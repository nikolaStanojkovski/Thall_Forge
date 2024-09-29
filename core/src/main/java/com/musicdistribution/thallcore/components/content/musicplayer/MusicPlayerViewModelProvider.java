package com.musicdistribution.thallcore.components.content.musicplayer;

import com.musicdistribution.thallcore.components.ViewModelProvider;
import com.musicdistribution.thallcore.components.shared.audio.AudioDurationViewModel;
import com.musicdistribution.thallcore.components.shared.audio.AudioViewModel;
import com.musicdistribution.thallcore.components.shared.audio.AudioViewModelProvider;
import com.musicdistribution.thallcore.components.shared.image.ImageViewModel;
import com.musicdistribution.thallcore.components.shared.image.ImageViewModelProvider;
import com.musicdistribution.thallcore.utils.NavUtils;
import com.musicdistribution.thallcore.utils.ResourceUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.Optional;

@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MusicPlayerViewModelProvider implements ViewModelProvider<MusicPlayerViewModel> {

    @NonNull
    private final SlingHttpServletRequest request;

    @NonNull
    private final ResourceResolver resourceResolver;

    @NonNull
    private final Resource resource;

    @Override
    public MusicPlayerViewModel getViewModel() {
        MusicPlayerResourceModel resourceModel = resource.adaptTo(MusicPlayerResourceModel.class);
        if (resourceModel != null) {
            MusicPlayerTrackResourceModel trackResourceModel = resourceModel.getTrackInfo();
            if (trackResourceModel != null) {
                AudioViewModel audioTrackModel = AudioViewModelProvider.builder()
                        .audioResource(trackResourceModel.getAudioTrack())
                        .build().getViewModel();
                if (hasTrackContent(trackResourceModel, audioTrackModel)) {
                    return createViewModelWithContent(resourceModel, trackResourceModel, audioTrackModel);
                }
            }
        }
        return createViewModelWithoutContent();
    }

    private boolean hasTrackContent(MusicPlayerTrackResourceModel trackResourceModel, AudioViewModel audioTrackModel) {
        return StringUtils.isNoneBlank(trackResourceModel.getTitle(), trackResourceModel.getArtist())
                && audioTrackModel.isHasContent();
    }

    private MusicPlayerViewModel createViewModelWithContent(MusicPlayerResourceModel resourceModel,
                                                            MusicPlayerTrackResourceModel trackResourceModel,
                                                            AudioViewModel audioTrackModel) {

        return MusicPlayerViewModel.builder()
                .resourceId(ResourceUtils.generateId(resource))
                .trackTitle(trackResourceModel.getTitle())
                .trackArtist(trackResourceModel.getArtist())
                .trackDuration(getTrackDuration(audioTrackModel))
                .trackAudioLink(getTrackAudioLink(audioTrackModel))
                .trackCoverLink(getTrackCoverLink(trackResourceModel))
                .browserSupportErrorMessage(resourceModel.getBrowserSupportErrorMessage())
                .playLabel(resourceModel.getPlayLabel())
                .stopLabel(resourceModel.getStopLabel())
                .volumeControlLabel(resourceModel.getVolumeControlLabel())
                .repeatLabel(resourceModel.getRepeatLabel())
                .enableShuffle(resourceModel.isEnableShuffle())
                .shuffleAlbumPath(getShuffleAlbumPath(resourceModel))
                .shuffleLabel(resourceModel.getShuffleLabel())
                .hasContent(true)
                .build();
    }

    private String getTrackDuration(AudioViewModel audioTrackModel) {
        return Optional.ofNullable(audioTrackModel.getDuration())
                .map(AudioDurationViewModel::toString)
                .filter(StringUtils::isNotBlank)
                .orElse("00:00");
    }

    private String getShuffleAlbumPath(MusicPlayerResourceModel resourceModel) {
        return getPathLink(resourceModel.getShuffleAlbumPath());
    }

    private String getTrackCoverLink(MusicPlayerTrackResourceModel trackResourceModel) {
        ImageViewModel imageModel = ImageViewModelProvider.builder()
                .imageResource(trackResourceModel.getTrackCover())
                .build()
                .getViewModel();
        if (imageModel != null && imageModel.isHasContent()) {
            return getPathLink(imageModel.getLink());
        } else {
            return StringUtils.EMPTY;
        }
    }

    private String getTrackAudioLink(AudioViewModel audioTrackModel) {
        if (audioTrackModel.isHasContent()) {
            return getPathLink(audioTrackModel.getLink());
        } else {
            return StringUtils.EMPTY;
        }
    }

    private String getPathLink(String path) {
        return Optional.ofNullable(path)
                .filter(StringUtils::isNotBlank)
                .map(resourceResolver::getResource)
                .map(Resource::getPath)
                .map(NavUtils::addHtmlExtension)
                .orElse(StringUtils.EMPTY);
    }

    private MusicPlayerViewModel createViewModelWithoutContent() {
        return MusicPlayerViewModel.builder().hasContent(false).build();
    }
}
