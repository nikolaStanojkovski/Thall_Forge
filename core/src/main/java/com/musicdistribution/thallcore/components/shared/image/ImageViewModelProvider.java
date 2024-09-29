package com.musicdistribution.thallcore.components.shared.image;

import com.musicdistribution.thallcore.components.ViewModelProvider;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.apache.sling.api.resource.Resource;

@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageViewModelProvider implements ViewModelProvider<ImageViewModel> {

    private final Resource imageResource;

    @Override
    public ImageViewModel getViewModel() {
        return null;
    }
}
