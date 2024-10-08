package com.musicdistribution.thallcore.components.content.genreexplorer;

import com.musicdistribution.thallcore.components.ViewModelProvider;
import com.musicdistribution.thallcore.components.shared.audio.AudioDurationViewModel;
import com.musicdistribution.thallcore.components.shared.audio.AudioViewModel;
import com.musicdistribution.thallcore.components.shared.audio.AudioViewModelProvider;
import com.musicdistribution.thallcore.components.shared.genre.Genre;
import com.musicdistribution.thallcore.components.shared.image.ImageViewModel;
import com.musicdistribution.thallcore.components.shared.image.ImageViewModelProvider;
import com.musicdistribution.thallcore.constants.ThallforgeConstants;
import com.musicdistribution.thallcore.services.ResourceResolverRetrievalService;
import com.musicdistribution.thallcore.utils.NavUtils;
import com.musicdistribution.thallcore.utils.ResourceUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GenreExplorerViewModelProvider implements ViewModelProvider<GenreExplorerViewModel> {

    @NonNull
    private final SlingHttpServletRequest request;

    @NonNull
    private final Resource resource;

    @NonNull
    private final ResourceResolverRetrievalService resourceResolverRetrievalService;

    @Override
    public GenreExplorerViewModel getViewModel() {
        return Optional.ofNullable(resource.adaptTo(GenreExplorerResourceModel.class))
                .map(this::createViewModelWithContent)
                .orElseGet(this::createViewModelWithoutContent);
    }

    private GenreExplorerViewModel createViewModelWithContent(GenreExplorerResourceModel resourceModel) {
        List<GenreExplorerAlbumViewModel> albums = getAlbums(resourceModel);
        return GenreExplorerViewModel.builder()
                .selectedGenre(getSelectedGenre(resourceModel))
                .albums(albums)
                // TODO: Do the rest of the mapping
                .albumSongs(Collections.emptyList())
                .hasContent(!albums.isEmpty())
                .build();
    }

    private List<GenreExplorerAlbumViewModel> getAlbums(GenreExplorerResourceModel resourceModel) {
        List<Resource> results = resourceResolverRetrievalService.getAdministrativeResourceResolver()
                .map(resourceResolver -> resourceResolver.findResources(getQuery(resourceModel), "JCR-SQL2"))
                .map(IteratorUtils::toList)
                .orElse(Collections.emptyList());
        results.stream()
                .map(resource -> GenreExplorerAlbumViewModel.builder()
                        .id(ResourceUtils.generateId(resource))
                        .title(resource.getName())
                        .thumbnail()
                        .build())
    }

    private String getQuery(GenreExplorerResourceModel resourceModel) {
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

    private String getSelectedGenre(GenreExplorerResourceModel resourceModel) {
        return Optional.ofNullable(resourceModel.getGenre())
                .map(Genre::valueOf)
                .map(Genre::getTitle)
                .orElse(StringUtils.EMPTY);
    }

    private GenreExplorerViewModel createViewModelWithoutContent() {
        return GenreExplorerViewModel.builder().hasContent(false).build();
    }
}
