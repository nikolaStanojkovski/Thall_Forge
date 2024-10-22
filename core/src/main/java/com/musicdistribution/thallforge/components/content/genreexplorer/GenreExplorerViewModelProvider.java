package com.musicdistribution.thallforge.components.content.genreexplorer;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.commons.util.DamUtil;
import com.musicdistribution.thallforge.components.ViewModelProvider;
import com.musicdistribution.thallforge.components.shared.genre.Genre;
import com.musicdistribution.thallforge.constants.ThallforgeConstants;
import com.musicdistribution.thallforge.services.AlbumTrackListService;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import com.musicdistribution.thallforge.utils.ImageUtils;
import com.musicdistribution.thallforge.utils.QueryUtils;
import com.musicdistribution.thallforge.utils.ResourceUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
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
                .albumSongs(albumSongs)
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
        try {
            String albumSearchQuery = getAlbumSearchQuery(resourceModel);
            List<GenreExplorerAlbumViewModel> resultList = new ArrayList<>();
            NodeIterator nodeIterator
                    = QueryUtils.executeQuery(resourceResolver, albumSearchQuery, resourceModel.getLimit());
            if (nodeIterator == null) {
                return Collections.emptyList();
            }
            while (nodeIterator.hasNext()) {
                Node node = nodeIterator.nextNode();
                Resource resultResource = resourceResolver.getResource(node.getPath());
                resultList.add(getAlbumViewModel(resultResource, resourceResolver));
            }
            return resultList;
        } catch (RepositoryException e) {
            log.error("Could not query album tracks for genre {}", resourceModel.getGenre(), e);
        }
        return Collections.emptyList();
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
        return Optional.ofNullable(albumResource.getChild("jcr:content"))
                .map(this::getAlbumThumbnailPath)
                .map(resourceResolver::getResource)
                .filter(ImageUtils::isImageResource)
                .map(Resource::getPath)
                .orElse(StringUtils.EMPTY);
    }

    private String getAlbumThumbnailPath(Resource albumContentResource) {
        return String.format("%s/manualThumbnail.%s",
                albumContentResource.getPath(), ThallforgeConstants.Extensions.JPG);
    }

    private String getSelectedGenre(GenreExplorerResourceModel resourceModel) {
        return Arrays.stream(Genre.values())
                .filter(g -> g.getName().equals(resourceModel.getGenre()))
                .findAny()
                .map(Genre::getTitle)
                .orElse(StringUtils.EMPTY);

    }

    private String getAlbumSearchQuery(GenreExplorerResourceModel resourceModel) {
        StringBuilder sb = new StringBuilder();
        String genre = Optional.ofNullable(resourceModel.getGenre()).orElse(StringUtils.EMPTY);
        sb.append("SELECT * FROM [nt:folder] AS albumNode WHERE ISDESCENDANTNODE(albumNode, '/content/dam') ");
        sb.append(String.format("AND albumNode.[jcr:content/metadata/genre] = '%s' ", genre));
        if (resourceModel.isSort()) {
            sb.append("ORDER BY albumNode.[jcr:content/jcr:title] DESC ");
        }
        return sb.toString();
    }

    private GenreExplorerViewModel createViewModelWithoutContent() {
        return GenreExplorerViewModel.builder().hasContent(false).build();
    }
}
