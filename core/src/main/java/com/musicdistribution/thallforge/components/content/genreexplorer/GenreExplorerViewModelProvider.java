package com.musicdistribution.thallforge.components.content.genreexplorer;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.commons.util.DamUtil;
import com.musicdistribution.thallforge.components.ViewModelProvider;
import com.musicdistribution.thallforge.components.shared.genre.Genre;
import com.musicdistribution.thallforge.services.AlbumTrackListService;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import com.musicdistribution.thallforge.utils.ResourceUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GenreExplorerViewModelProvider implements ViewModelProvider<GenreExplorerViewModel> {

    @NonNull
    private final Resource resource;

    @NonNull
    private final ResourceResolverRetrievalService resourceResolverRetrievalService;

    @NonNull
    private final AlbumTrackListService albumTrackListService;

    @Override
    public GenreExplorerViewModel getViewModel() {
        return Optional.ofNullable(resource.adaptTo(GenreExplorerResourceModel.class))
                .flatMap(resourceModel -> resourceResolverRetrievalService.getContentDamResourceResolver()
                        .map(resourceResolver -> createViewModelWithContent(resourceModel, resourceResolver)))
                .orElseGet(this::createViewModelWithoutContent);
    }

    private GenreExplorerViewModel createViewModelWithContent(GenreExplorerResourceModel resourceModel,
                                                              ResourceResolver resourceResolver) {
        List<GenreExplorerAlbumViewModel> albums = getAlbums(resourceModel, resourceResolver);
        List<GenreExplorerAlbumSongsViewModel> albumSongs = getAlbumSongs(albums);
        return GenreExplorerViewModel.builder()
                .selectedGenre(getSelectedGenre(resourceModel))
                .albums(albums)
                .albumSongs(Collections.emptyList())
                .hasContent(hasContent(albums, albumSongs))
                .build();
    }

    private List<GenreExplorerAlbumSongsViewModel> getAlbumSongs(List<GenreExplorerAlbumViewModel> albums) {
        return albums.stream()
                .map(this::getAlbumSongsViewModel)
                .collect(Collectors.toList());
    }

    private GenreExplorerAlbumSongsViewModel getAlbumSongsViewModel(GenreExplorerAlbumViewModel albumViewModel) {
        return GenreExplorerAlbumSongsViewModel.builder()
                .albumId(albumViewModel.getId())
                .songs(albumTrackListService.getTracks(albumViewModel.getLink()))
                .build();
    }

    private List<GenreExplorerAlbumViewModel> getAlbums(GenreExplorerResourceModel resourceModel,
                                                        ResourceResolver resourceResolver) {
        return IteratorUtils.toList(resourceResolver
                        .findResources(getAlbumSearchQuery(resourceModel), "JCR-SQL2"))
                .stream()
                .map(albumResource -> getAlbumViewModel(albumResource, resourceResolver))
                .collect(Collectors.toList());
    }

    private GenreExplorerAlbumViewModel getAlbumViewModel(Resource resource, ResourceResolver resourceResolver) {
        return GenreExplorerAlbumViewModel.builder()
                .id(ResourceUtils.generateId(resource))
                .link(resource.getPath())
                .title(resource.getName())
                .artist(getAlbumArtist(resource))
                .thumbnail(getAlbumThumbnail(resource, resourceResolver))
                .build();
    }

    private boolean hasContent(List<GenreExplorerAlbumViewModel> albums,
                               List<GenreExplorerAlbumSongsViewModel> albumSongs) {
        return !albums.isEmpty() && !albumSongs.isEmpty();
    }

    private String getAlbumArtist(Resource albumResource) {
        return Optional.ofNullable(albumResource.adaptTo(Asset.class))
                .map(albumAsset -> albumAsset.getMetadataValue("artist"))
                .orElse(StringUtils.EMPTY);
    }

    private String getAlbumThumbnail(Resource albumResource, ResourceResolver resourceResolver) {
        return Optional.ofNullable(albumResource.adaptTo(ValueMap.class))
                .map(properties -> properties.get("jcr:thumbnail", String.class))
                .filter(thumbnail -> isValidAlbumThumbnail(resourceResolver, thumbnail))
                .orElse(StringUtils.EMPTY);
    }

    private boolean isValidAlbumThumbnail(ResourceResolver resourceResolver,
                                          String albumThumbnail) {
        return Optional.ofNullable(resourceResolver.getResource(albumThumbnail))
                .map(r -> r.adaptTo(Asset.class))
                .map(DamUtil::isImage)
                .orElse(false);
    }

    private String getSelectedGenre(GenreExplorerResourceModel resourceModel) {
        return Optional.ofNullable(resourceModel.getGenre())
                .map(Genre::valueOf)
                .map(Genre::getTitle)
                .orElse(StringUtils.EMPTY);
    }

    private String getAlbumSearchQuery(GenreExplorerResourceModel resourceModel) {
        StringBuilder sb = new StringBuilder();
        String genre = Optional.ofNullable(resourceModel.getGenre()).orElse(StringUtils.EMPTY);
        sb.append("SELECT * FROM [nt:folder] AS albumNode WHERE ISDESCENDANTNODE(albumNode, '/content/dam') ");
        sb.append(String.format("AND albumNode.[jcr:content/metadata/genre] = '%s' ", genre));
        if (resourceModel.isSort()) {
            sb.append("ORDER BY albumNode.[jcr:content/metadata/title] DESC ");
        }
        sb.append(String.format("LIMIT %d", resourceModel.getLimit()));
        return sb.toString();
    }

    private GenreExplorerViewModel createViewModelWithoutContent() {
        return GenreExplorerViewModel.builder().hasContent(false).build();
    }
}
