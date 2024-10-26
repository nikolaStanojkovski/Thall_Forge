package com.musicdistribution.thallforge.components.content.artistspotlight;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class ArtistSpotlightSongViewModel {

    private final String link;
    private final String title;
}
