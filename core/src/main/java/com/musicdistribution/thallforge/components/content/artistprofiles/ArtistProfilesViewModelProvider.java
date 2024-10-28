package com.musicdistribution.thallforge.components.content.artistprofiles;

import com.musicdistribution.thallforge.components.ViewModelProvider;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;

import java.util.Optional;

@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ArtistProfilesViewModelProvider implements ViewModelProvider<ArtistProfilesViewModel> {

    @NonNull
    private final Resource resource;

    @Override
    public ArtistProfilesViewModel getViewModel() {
        return Optional.ofNullable(resource.adaptTo(ArtistProfilesResourceModel.class))
                .filter(this::hasContent)
                .map(this::createViewModelWithContent)
                .orElseGet(this::createViewModelWithoutContent);
    }

    private boolean hasContent(ArtistProfilesResourceModel resourceModel) {
        return StringUtils.isNotBlank(resourceModel.getTitle());
    }

    private ArtistProfilesViewModel createViewModelWithContent(ArtistProfilesResourceModel resourceModel) {
        return ArtistProfilesViewModel.builder()
                .title(resourceModel.getTitle())
                .hasContent(true)
                .build();
    }

    private ArtistProfilesViewModel createViewModelWithoutContent() {
        return ArtistProfilesViewModel.builder().hasContent(false).build();
    }
}
