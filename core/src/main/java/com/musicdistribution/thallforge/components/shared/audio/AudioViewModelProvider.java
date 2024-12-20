package com.musicdistribution.thallforge.components.shared.audio;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.musicdistribution.thallforge.components.ViewModelProvider;
import com.musicdistribution.thallforge.utils.AudioUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;

import javax.jcr.RepositoryException;
import java.io.InputStream;
import java.util.Optional;

@Slf4j
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
        return getBinaryInputStream(audioAsset)
                .map(AudioUtils::getDurationSeconds)
                .filter(duration -> duration > 0)
                .map(duration -> AudioDurationViewModel.builder()
                        .minutes(duration / 60)
                        .seconds(duration % 60)
                        .build())
                .orElse(AudioDurationViewModel.builder().build());
    }

    private Optional<InputStream> getBinaryInputStream(Asset audioAsset) {
        return Optional.ofNullable(audioAsset.getOriginal())
                .map(Rendition::getBinary)
                .map(binary -> {
                    try {
                        return binary.getStream();
                    } catch (RepositoryException e) {
                        log.error("Could not read the binary stream from the asset {} with path {}",
                                audioAsset.getName(), audioAsset.getPath(), e);
                    }
                    return null;
                });
    }

    private AudioViewModel createViewModelWithoutContent() {
        return AudioViewModel.builder()
                .hasContent(false)
                .build();
    }
}
