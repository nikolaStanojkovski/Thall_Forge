package com.musicdistribution.thallforge.components.content.artistspotlight;

import com.musicdistribution.thallforge.components.ViewModelProvider;
import com.musicdistribution.thallforge.components.shared.artist.ArtistContentFragmentViewModel;
import com.musicdistribution.thallforge.components.shared.artist.ArtistContentFragmentViewModelProvider;
import com.musicdistribution.thallforge.constants.ThallforgeConstants;
import com.musicdistribution.thallforge.services.AlbumQueryService;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import com.musicdistribution.thallforge.services.impl.models.AlbumViewModel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

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
        return Optional.ofNullable(resource.adaptTo(ArtistSpotlightResourceModel.class))
                .flatMap(resourceModel -> resourceResolverRetrievalService.getContentDamResourceResolver()
                        .map(resourceResolver -> createViewModelWithContent(resourceModel, resourceResolver)))
                .orElseGet(this::createViewModelWithoutContent);
    }

    private ArtistSpotlightViewModel createViewModelWithContent(ArtistSpotlightResourceModel resourceModel,
                                                                ResourceResolver resourceResolver) {
        String artistReference = resourceModel.getArtistReference();
        List<AlbumViewModel> albums = albumQueryService.getAlbums(
                resourceResolver,
                getAlbumSearchQuery(artistReference),
                ThallforgeConstants.SqlQuery.DEFAULT_LIMIT);
        ArtistContentFragmentViewModel artistViewModel = getArtistData(artistReference, resourceResolver);
        return ArtistSpotlightViewModel.builder()
                .artistName(artistViewModel.getName())
                .artistImage(artistViewModel.getImage())
                .artistBiography(artistViewModel.getBiography())
                .albums(albums)
                .hasContent(hasContent(albums, artistViewModel))
                .build();
    }

    private boolean hasContent(List<AlbumViewModel> albums,
                               ArtistContentFragmentViewModel artistViewModel) {
        return !albums.isEmpty() && artistViewModel.isHasContent();
    }

    private ArtistContentFragmentViewModel getArtistData(String artistReference, ResourceResolver resourceResolver) {
        return Optional.ofNullable(resourceResolver.getResource(artistReference))
                .map(artistContentFragmentResource -> ArtistContentFragmentViewModelProvider.builder()
                        .artistContentFragmentResource(artistContentFragmentResource)
                        .resolver(resourceResolver)
                        .build().getViewModel())
                .orElse(ArtistContentFragmentViewModel.builder().hasContent(false).build());
    }

    private String getAlbumSearchQuery(String artistReference) {
        return "SELECT * FROM [nt:folder] AS albumNode WHERE ISDESCENDANTNODE(albumNode, '/content/dam') "
                + String.format("AND albumNode.[jcr:content/folderMetadataSchema] = '%s' AND albumNode.[jcr:content/metadata/artist] = '%s'",
                ThallforgeConstants.MetadataSchema.ALBUM_METADATA_SCHEMA, artistReference);
    }

    private ArtistSpotlightViewModel createViewModelWithoutContent() {
        return ArtistSpotlightViewModel.builder().hasContent(false).build();
    }
}
