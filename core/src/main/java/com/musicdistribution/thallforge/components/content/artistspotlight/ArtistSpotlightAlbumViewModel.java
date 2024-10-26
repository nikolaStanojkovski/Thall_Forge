package com.musicdistribution.thallforge.components.content.artistspotlight;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class ArtistSpotlightAlbumViewModel {

    private final String title;
    private final String thumbnail;
    private final List<ArtistSpotlightSongViewModel> songs;
    private final boolean hasContent;
}
