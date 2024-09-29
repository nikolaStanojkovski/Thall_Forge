package com.musicdistribution.thallcore.components.shared.audio;

import com.musicdistribution.thallcore.components.ViewModelProvider;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.apache.sling.api.resource.Resource;

@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AudioViewModelProvider implements ViewModelProvider<AudioViewModel> {

    private final Resource audioResource;

    @Override
    public AudioViewModel getViewModel() {
        return null;
    }
}
