package com.musicdistribution.thallforge.components.content.artistprofiles.artistprofilesitem;

import com.musicdistribution.thallforge.components.ViewModelProvider;
import com.musicdistribution.thallforge.components.shared.artist.ArtistContentFragmentViewModel;
import com.musicdistribution.thallforge.services.ArtistQueryService;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;

import java.util.Optional;

@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ArtistProfilesItemViewModelProvider implements ViewModelProvider<ArtistProfilesItemViewModel> {

    @NonNull
    private final Resource resource;

    @NonNull
    private final ResourceResolverRetrievalService resourceResolverRetrievalService;

    @NonNull
    private final ArtistQueryService artistQueryService;

    @Override
    public ArtistProfilesItemViewModel getViewModel() {
        return Optional.ofNullable(resource.adaptTo(ArtistProfilesItemResourceModel.class))
                .filter(this::hasContent)
                .map(this::createViewModel)
                .orElseGet(this::createViewModelWithoutContent);
    }

    private boolean hasContent(ArtistProfilesItemResourceModel resourceModel) {
        return StringUtils.isNotBlank(resourceModel.getArtistReference());
    }

    private ArtistProfilesItemViewModel createViewModel(ArtistProfilesItemResourceModel resourceModel) {
        String artistReference = resourceModel.getArtistReference();
        return resourceResolverRetrievalService.getContentDamResourceResolver()
                .flatMap(resourceResolver -> artistQueryService.getArtistData(resourceResolver, artistReference)
                        .map(this::createViewModelWithContent))
                .orElseGet(this::createViewModelWithoutContent);
    }

    private ArtistProfilesItemViewModel createViewModelWithContent(ArtistContentFragmentViewModel artist) {
        return ArtistProfilesItemViewModel.builder()
                .artist(artist)
                .hasContent(true)
                .build();
    }

    private ArtistProfilesItemViewModel createViewModelWithoutContent() {
        return ArtistProfilesItemViewModel.builder().hasContent(false).build();
    }
}
