package com.musicdistribution.thallcore.components.shared.audio;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.commons.util.DamUtil;
import com.musicdistribution.thallcore.components.ViewModelProvider;
import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.Optional;

@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AudioViewModelProvider implements ViewModelProvider<AudioViewModel> {

    private final Resource audioResource;

    @Override
    public AudioViewModel getViewModel() {
        return Optional.ofNullable(audioResource)
                .map(r -> r.adaptTo(AudioResourceModel.class))
                .map(this::createViewModelWithContent)
                .orElseGet(this::createViewModelWithoutContent);
    }

    private AudioViewModel createViewModelWithContent(AudioResourceModel resourceModel) {
        return getAudioAsset(resourceModel)
                .map(asset -> AudioViewModel.builder()
                        .title(getTitle(asset))
                        .link(asset.getPath())
                        .duration(getDuration(resourceModel))
                        .hasContent(true)
                        .build())
                .orElseGet(this::createViewModelWithoutContent);
    }

    private String getTitle(Asset audioAsset) {
        return Optional.ofNullable(audioAsset.getMetadataValue("jcr:title"))
                .filter(StringUtils::isNotBlank)
                .orElse(audioAsset.getName());
    }

    private AudioDurationViewModel getDuration(AudioResourceModel resourceModel) {
        return Optional.ofNullable(resourceModel.getDuration())
                .filter(StringUtils::isNotBlank)
                .map(Integer::parseInt)
                .map(duration -> AudioDurationViewModel.builder()
                        .minutes(duration / 60)
                        .seconds(duration % 60)
                        .build())
                .orElse(AudioDurationViewModel.builder().build());
    }

    private Optional<Asset> getAudioAsset(AudioResourceModel resourceModel) {
        String fileReference = resourceModel.getFileReference();
        ResourceResolver resourceResolver = audioResource.getResourceResolver();
        return Optional.ofNullable(resourceResolver.getResource(fileReference))
                .filter(DamUtil::isAsset)
                .map(r -> r.adaptTo(Asset.class))
                .filter(this::isAudioAsset);
    }

    private boolean isAudioAsset(Asset asset) {
        List<String> audioMimeTypes = Arrays.asList("audio/aac", "audio/mpeg", "audio/ogg", "audio/wav");
        String mimeType = asset.getMimeType();
        if (StringUtils.isBlank(mimeType)) {
            return false;
        }
        return audioMimeTypes.contains(mimeType);
    }

    private AudioViewModel createViewModelWithoutContent() {
        return AudioViewModel.builder()
                .hasContent(false)
                .build();
    }
}
