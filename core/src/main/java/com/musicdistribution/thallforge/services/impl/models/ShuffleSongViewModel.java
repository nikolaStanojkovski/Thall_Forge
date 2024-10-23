package com.musicdistribution.thallforge.services.impl.models;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class ShuffleSongViewModel {

    private final String title;
    private final String artist;
    private final String coverLink;
    private final String trackLink;
}
