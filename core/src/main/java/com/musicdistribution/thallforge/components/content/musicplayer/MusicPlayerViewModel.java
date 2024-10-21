package com.musicdistribution.thallforge.components.content.musicplayer;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class MusicPlayerViewModel {

    private final String resourceId;
    private final String trackTitle;
    private final String trackArtist;
    private final String trackDuration;
    private final String trackAudioLink;
    private final String trackCoverLink;
    private final String browserSupportErrorMessage;
    private final String playLabel;
    private final String stopLabel;
    private final String volumeControlLabel;
    private final String repeatLabel;
    private final boolean enableShuffle;
    private final String shuffleEndpointPath;
    private final String shuffleLabel;
    private final boolean hasContent;
}
