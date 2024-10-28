package com.musicdistribution.thallforge.components.content.genreexplorer;

import com.musicdistribution.thallforge.components.ViewModelProvider;
import com.musicdistribution.thallforge.components.shared.genre.Genre;
import com.musicdistribution.thallforge.constants.ThallforgeConstants;
import com.musicdistribution.thallforge.services.AlbumQueryService;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import com.musicdistribution.thallforge.services.impl.models.AlbumViewModel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.Arrays;
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
    private final AlbumQueryService albumQueryService;

    @Override
    public GenreExplorerViewModel getViewModel() {
        return Optional.ofNullable(resource.adaptTo(GenreExplorerResourceModel.class))
                .flatMap(resourceModel -> resourceResolverRetrievalService.getContentDamResourceResolver()
                        .map(resourceResolver -> createViewModelWithContent(resourceResolver, resourceModel)))
                .orElseGet(this::createViewModelWithoutContent);
    }

    private GenreExplorerViewModel createViewModelWithContent(ResourceResolver resourceResolver,
                                                              GenreExplorerResourceModel resourceModel) {
        List<AlbumViewModel> albums = albumQueryService.getAlbums(
                resourceResolver, getAlbumSearchQuery(resourceModel), resourceModel.getLimit());
        List<GenreExplorerAlbumSongsViewModel> albumSongs = getAlbumSongs(albums);
        return GenreExplorerViewModel.builder()
                .selectedGenre(getSelectedGenre(resourceModel))
                .albums(albums)
                .albumSongs(albumSongs)
                .hasContent(hasContent(albums, albumSongs))
                .build();
    }

    private List<GenreExplorerAlbumSongsViewModel> getAlbumSongs(List<AlbumViewModel> albums) {
        return albums.stream()
                .map(this::getAlbumSongsViewModel)
                .collect(Collectors.toList());
    }

    private GenreExplorerAlbumSongsViewModel getAlbumSongsViewModel(AlbumViewModel albumViewModel) {
        return GenreExplorerAlbumSongsViewModel.builder()
                .albumId(albumViewModel.getId())
                .songs(albumViewModel.getSongs())
                .build();
    }

    private boolean hasContent(List<AlbumViewModel> albums,
                               List<GenreExplorerAlbumSongsViewModel> albumSongs) {
        return !albums.isEmpty() && !albumSongs.isEmpty();
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
        sb.append(String.format("AND albumNode.[jcr:content/folderMetadataSchema] = '%s'",
                ThallforgeConstants.MetadataSchema.ALBUM_METADATA_SCHEMA));
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
