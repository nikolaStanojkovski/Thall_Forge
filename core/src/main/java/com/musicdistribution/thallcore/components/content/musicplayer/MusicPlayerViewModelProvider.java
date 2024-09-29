package com.musicdistribution.thallcore.components.content.musicplayer;

import com.musicdistribution.thallcore.components.ViewModelProvider;
import com.musicdistribution.thallcore.components.shared.audio.AudioViewModel;
import com.musicdistribution.thallcore.components.shared.audio.AudioViewModelProvider;
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
                .trackDuration(audioTrackModel.getDuration().toString())
                .trackAudioLink(audioTrackModel.getLink())
                .trackCoverLink(getTrackCoverLink(resourceModel))
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

    private String getShuffleAlbumPath(MusicPlayerResourceModel resourceModel) {
        return Optional.ofNullable(resourceModel.getShuffleAlbumPath())
                .filter(StringUtils::isNotBlank)
                .map(resourceResolver::getResource)
                .map(Resource::getPath)
                .map(NavUtils::addHtmlExtension)
                .orElse(StringUtils.EMPTY);
    }

    private String getTrackCoverLink(MusicPlayerResourceModel resourceModel) {
        // TODO: Implement logic for getting the track-cover link
    }

    private MusicPlayerViewModel createViewModelWithoutContent() {
        return MusicPlayerViewModel.builder().hasContent(false).build();
    }
}
