package com.musicdistribution.thallforge.components.content.artistspotlight;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class ArtistSpotlightViewModel {

    private final String artistImage;
    private final String artistName;
    private final String artistBiography;
    private final List<ArtistSpotlightAlbumViewModel> albums;
    private final boolean hasContent;
}
