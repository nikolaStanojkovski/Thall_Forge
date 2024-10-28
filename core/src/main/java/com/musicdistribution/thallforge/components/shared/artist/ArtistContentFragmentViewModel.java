package com.musicdistribution.thallforge.components.shared.artist;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class ArtistContentFragmentViewModel {

    private final String path;
    private final String biography;
    private final String image;
    private final String name;
    private final Integer rating;
    private final boolean hasContent;
}
