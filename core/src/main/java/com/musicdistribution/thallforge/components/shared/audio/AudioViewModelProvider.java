package com.musicdistribution.thallforge.components.shared.audio;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.musicdistribution.thallforge.components.ViewModelProvider;
import com.musicdistribution.thallforge.utils.AudioUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;

import java.util.Optional;

@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AudioViewModelProvider implements ViewModelProvider<AudioViewModel> {

    private final Resource audioResource;

    @Override
    public AudioViewModel getViewModel() {
        return Optional.ofNullable(audioResource)
                .map(r -> createViewModelWithContent())
                .orElseGet(this::createViewModelWithoutContent);
    }

    private AudioViewModel createViewModelWithContent() {
        return Optional.ofNullable(audioResource.adaptTo(Asset.class))
                .map(asset -> AudioViewModel.builder()
                        .title(getTitle(asset))
                        .link(asset.getPath())
                        .duration(getDuration(asset))
                        .hasContent(true)
                        .build())
                .orElseGet(this::createViewModelWithoutContent);
    }

    private String getTitle(Asset audioAsset) {
        return Optional.ofNullable(audioAsset.getMetadataValue("jcr:title"))
                .filter(StringUtils::isNotBlank)
                .orElse(audioAsset.getName());
    }

    private AudioDurationViewModel getDuration(Asset audioAsset) {
        return Optional.ofNullable(audioAsset.getOriginal())
                .map(Rendition::getStream)
                .map(AudioUtils::getDuration)
                .filter(duration -> duration > 0)
                .map(duration -> AudioDurationViewModel.builder()
                        .minutes(duration / 60)
                        .seconds(duration % 60)
                        .build())
                .orElse(AudioDurationViewModel.builder().build());
    }

    private AudioViewModel createViewModelWithoutContent() {
        return AudioViewModel.builder()
                .hasContent(false)
                .build();
    }
}
