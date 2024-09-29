package com.musicdistribution.thallcore.components.shared.image;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.commons.util.DamUtil;
import com.musicdistribution.thallcore.components.ViewModelProvider;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.Optional;

@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageViewModelProvider implements ViewModelProvider<ImageViewModel> {

    private final Resource imageResource;

    @Override
    public ImageViewModel getViewModel() {
        return Optional.ofNullable(imageResource)
                .map(r -> r.adaptTo(ImageResourceModel.class))
                .map(this::createViewModelWithContent)
                .orElseGet(this::createViewModelWithoutContent);
    }

    private ImageViewModel createViewModelWithContent(ImageResourceModel resourceModel) {
        return getImageAsset(resourceModel)
                .map(asset -> ImageViewModel.builder()
                        .link(asset.getPath())
                        .alt(StringUtils.defaultString(resourceModel.getAlt()))
                        .caption(StringUtils.defaultString(resourceModel.getCaption()))
                        .hasContent(true)
                        .build())
                .orElseGet(this::createViewModelWithoutContent);
    }

    private Optional<Asset> getImageAsset(ImageResourceModel resourceModel) {
        String fileReference = resourceModel.getFileReference();
        ResourceResolver resourceResolver = imageResource.getResourceResolver();
        return Optional.ofNullable(resourceResolver.getResource(fileReference))
                .filter(DamUtil::isAsset)
                .map(r -> r.adaptTo(Asset.class));
    }

    private ImageViewModel createViewModelWithoutContent() {
        return ImageViewModel.builder()
                .hasContent(false)
                .build();
    }
}
