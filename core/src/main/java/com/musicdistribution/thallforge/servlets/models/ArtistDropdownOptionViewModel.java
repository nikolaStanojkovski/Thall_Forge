package com.musicdistribution.thallforge.servlets.models;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class ArtistDropdownOptionViewModel {

    private String key;
    private String value;
}
