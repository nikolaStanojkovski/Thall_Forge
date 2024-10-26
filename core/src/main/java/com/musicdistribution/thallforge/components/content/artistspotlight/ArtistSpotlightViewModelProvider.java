package com.musicdistribution.thallforge.components.content.artistspotlight;

import com.drew.lang.annotations.Nullable;
import com.musicdistribution.thallforge.components.ViewModelProvider;
import com.musicdistribution.thallforge.components.content.genreexplorer.GenreExplorerAlbumSongsViewModel;
import com.musicdistribution.thallforge.components.content.genreexplorer.GenreExplorerAlbumViewModel;
import com.musicdistribution.thallforge.components.content.genreexplorer.GenreExplorerResourceModel;
import com.musicdistribution.thallforge.components.content.genreexplorer.GenreExplorerViewModel;
import com.musicdistribution.thallforge.components.contentfragments.artist.ArtistContentFragmentResourceModel;
import com.musicdistribution.thallforge.components.shared.genre.Genre;
import com.musicdistribution.thallforge.constants.ThallforgeConstants;
import com.musicdistribution.thallforge.services.AlbumQueryService;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import com.musicdistribution.thallforge.services.impl.models.AlbumViewModel;
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

@Slf4j
@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ArtistSpotlightViewModelProvider implements ViewModelProvider<ArtistSpotlightViewModel> {

    @NonNull
    private final Resource resource;

    @NonNull
    private final ResourceResolverRetrievalService resourceResolverRetrievalService;

    @NonNull
    private final AlbumQueryService albumQueryService;

    @Override
    public ArtistSpotlightViewModel getViewModel() {
        return Optional.ofNullable(resource.adaptTo(GenreExplorerResourceModel.class))
                .flatMap(resourceModel -> resourceResolverRetrievalService.getContentDamResourceResolver()
                        .map(resourceResolver -> createViewModelWithContent(resourceModel, resourceResolver)))
                .orElseGet(this::createViewModelWithoutContent);
    }

    private ArtistSpotlightViewModel createViewModelWithContent(GenreExplorerResourceModel resourceModel,
                                                              ResourceResolver resourceResolver) {
        List<GenreExplorerAlbumViewModel> albums = getAlbums(resourceModel, resourceResolver);
        return ArtistSpotlightViewModel.builder()
                .albums(albums)
                .hasContent(hasContent(albums))
                .build();
    }

    private GenreExplorerViewModel createViewModelWithoutContent() {
        return GenreExplorerViewModel.builder().hasContent(false).build();
    }
}
