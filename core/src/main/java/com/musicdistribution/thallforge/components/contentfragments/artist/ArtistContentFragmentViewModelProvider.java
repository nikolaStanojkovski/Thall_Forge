package com.musicdistribution.thallforge.components.contentfragments.artist;

import com.musicdistribution.thallforge.components.ViewModelProvider;
import com.musicdistribution.thallforge.utils.ImageUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.Optional;

@Slf4j
@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ArtistContentFragmentViewModelProvider implements ViewModelProvider<ArtistContentFragmentViewModel> {

    private final ResourceResolver resolver;
    private final Resource artistContentFragmentResource;

    @Override
    public ArtistContentFragmentViewModel getViewModel() {
        return Optional.ofNullable(artistContentFragmentResource)
                .map(artistResource -> artistResource.getChild("jcr:content/data/master"))
                .map(this::createViewModelWithContent)
                .orElseGet(this::createViewModelWithoutContent);
    }

    private ArtistContentFragmentViewModel createViewModelWithContent(Resource artistContentFragmentResource) {
        return Optional.ofNullable(artistContentFragmentResource.adaptTo(ArtistContentFragmentResourceModel.class))
                .map(resourceModel -> ArtistContentFragmentViewModel.builder()
                        .path(artistContentFragmentResource.getPath())
                        .biography(StringUtils.defaultString(resourceModel.getBiography()))
                        .image(getArtistImage(resourceModel))
                        .name(resourceModel.getName())
                        .rating(resourceModel.getRating())
                        .hasContent(hasContent(resourceModel))
                        .build())
                .orElseGet(this::createViewModelWithoutContent);
    }

    private boolean hasContent(ArtistContentFragmentResourceModel resourceModel) {
        return resourceModel != null
                && StringUtils.isNoneBlank(resourceModel.getName(), resourceModel.getImage())
                && resourceModel.getRating() != null;
    }

    private String getArtistImage(ArtistContentFragmentResourceModel resourceModel) {
        return Optional.ofNullable(resolver.getResource(resourceModel.getImage()))
                .filter(ImageUtils::isImageAsset)
                .map(Resource::getPath)
                .orElse(StringUtils.EMPTY);
    }

    private ArtistContentFragmentViewModel createViewModelWithoutContent() {
        return ArtistContentFragmentViewModel.builder()
                .hasContent(false)
                .build();
    }
}
