package com.musicdistribution.thallforge.components.content.artistspotlight;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class ArtistSpotlightViewModel {

    private final String selectedGenre;
    private final List<ArtistSpotlightViewModel> albums;
    private final boolean hasContent;
}
