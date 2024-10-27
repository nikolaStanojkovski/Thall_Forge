package com.musicdistribution.thallforge.components.structure.topfansbanner;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class TopFansBannerViewModel {

    private final boolean displayBanner;
}
