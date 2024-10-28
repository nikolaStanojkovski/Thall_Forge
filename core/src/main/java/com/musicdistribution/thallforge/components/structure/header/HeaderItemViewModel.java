package com.musicdistribution.thallforge.components.structure.header;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class HeaderItemViewModel {

    private final String title;
    private final String link;
}
