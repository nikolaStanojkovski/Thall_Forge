package com.musicdistribution.thallforge.schedulers.models;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class ArtistDropdownOptionsViewModel {

    private final List<ArtistDropdownOptionViewModel> options;
}
