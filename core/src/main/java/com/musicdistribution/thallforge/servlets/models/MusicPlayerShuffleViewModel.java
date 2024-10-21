package com.musicdistribution.thallforge.servlets.models;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class MusicPlayerShuffleViewModel {

    private final String trackLink;
}
