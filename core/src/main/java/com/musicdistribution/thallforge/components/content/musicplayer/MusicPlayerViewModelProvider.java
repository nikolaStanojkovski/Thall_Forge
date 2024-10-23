package com.musicdistribution.thallforge.components.content.musicplayer;

import com.musicdistribution.thallforge.components.ViewModelProvider;
import com.musicdistribution.thallforge.components.shared.audio.AudioDurationViewModel;
import com.musicdistribution.thallforge.components.shared.audio.AudioViewModel;
import com.musicdistribution.thallforge.components.shared.audio.AudioViewModelProvider;
import com.musicdistribution.thallforge.constants.ThallforgeConstants;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import com.musicdistribution.thallforge.utils.ImageUtils;
import com.musicdistribution.thallforge.utils.ResourceUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.Optional;

@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MusicPlayerViewModelProvider implements ViewModelProvider<MusicPlayerViewModel> {

    @NonNull
    private final Resource resource;

    @NonNull
    private final ResourceResolverRetrievalService resourceResolverRetrievalService;

    @Override
    public MusicPlayerViewModel getViewModel() {
        return Optional.ofNullable(resource.adaptTo(MusicPlayerResourceModel.class))
                .flatMap(resourceModel -> Optional.ofNullable(resourceModel.getTrackInfo())
                        .map(trackResourceModel -> createViewModel(resourceModel, trackResourceModel)))
                .orElseGet(this::createViewModelWithoutContent);
    }

    private MusicPlayerViewModel createViewModel(MusicPlayerResourceModel resourceModel,
                                                 MusicPlayerTrackResourceModel trackResourceModel) {
        Optional<ResourceResolver> resourceResolver
                = resourceResolverRetrievalService.getContentDamResourceResolver();
        if (hasTrackContent(trackResourceModel) && resourceResolver.isPresent()) {
            Optional<AudioViewModel> audioTrackModel = getAudioTrackResource(trackResourceModel, resourceResolver.get());
            if (audioTrackModel.isPresent()) {
                return createViewModelWithContent(resourceModel, trackResourceModel,
                        audioTrackModel.get(), resourceResolver.get());
            }
        }
        return createViewModelWithoutContent();
    }

    private Optional<AudioViewModel> getAudioTrackResource(MusicPlayerTrackResourceModel trackResourceModel,
                                                           ResourceResolver resourceResolver) {
        return Optional.ofNullable(trackResourceModel.getAudioTrackFileReference())
                .map(resourceResolver::getResource)
                .map(r -> AudioViewModelProvider.builder()
                        .audioResource(r)
                        .build().getViewModel())
                .filter(AudioViewModel::isHasContent);
    }

    private boolean hasTrackContent(MusicPlayerTrackResourceModel trackResourceModel) {
        return StringUtils.isNoneBlank(trackResourceModel.getTitle(), trackResourceModel.getArtist());
    }

    private MusicPlayerViewModel createViewModelWithContent(MusicPlayerResourceModel resourceModel,
                                                            MusicPlayerTrackResourceModel trackResourceModel,
                                                            AudioViewModel audioTrackModel,
                                                            ResourceResolver resourceResolver) {

        return MusicPlayerViewModel.builder()
                .resourceId(ResourceUtils.generateId(resource))
                .trackTitle(trackResourceModel.getTitle())
                .trackArtist(trackResourceModel.getArtist())
                .trackDuration(getTrackDuration(audioTrackModel))
                .trackAudioLink(audioTrackModel.getLink())
                .trackCoverLink(getTrackCoverLink(trackResourceModel, resourceResolver))
                .browserSupportErrorMessage(resourceModel.getBrowserSupportErrorMessage())
                .playLabel(resourceModel.getPlayLabel())
                .stopLabel(resourceModel.getStopLabel())
                .volumeControlLabel(resourceModel.getVolumeControlLabel())
                .repeatLabel(resourceModel.getRepeatLabel())
                .enableShuffle(resourceModel.isEnableShuffle())
                .shuffleEndpointPath(getShuffleEndpointPath())
                .shuffleLabel(resourceModel.getShuffleLabel())
                .hasContent(true)
                .build();
    }

    private String getShuffleEndpointPath() {
        return String.format("%s.%s.%s", resource.getPath(),
                ThallforgeConstants.Endpoints.MUSIC_PLAYER_SHUFFLE_SELECTOR,
                ThallforgeConstants.Extensions.JSON);
    }

    private String getTrackDuration(AudioViewModel audioTrackModel) {
        return Optional.ofNullable(audioTrackModel.getDuration())
                .map(AudioDurationViewModel::toString)
                .filter(StringUtils::isNotBlank)
                .orElse("00:00");
    }

    private String getTrackCoverLink(MusicPlayerTrackResourceModel trackResourceModel,
                                     ResourceResolver resourceResolver) {
        return Optional.ofNullable(trackResourceModel.getTrackCoverFileReference())
                .map(resourceResolver::getResource)
                .map(Resource::getPath)
                .orElse(StringUtils.EMPTY);
    }

    private MusicPlayerViewModel createViewModelWithoutContent() {
        return MusicPlayerViewModel.builder().hasContent(false).build();
    }
}
