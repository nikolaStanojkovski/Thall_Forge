package com.musicdistribution.thallforge.schedulers.models;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class ArtistDropdownOptionViewModel {

    private final String value;
    private final String text;
}
