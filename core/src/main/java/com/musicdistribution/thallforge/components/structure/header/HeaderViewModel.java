package com.musicdistribution.thallforge.components.structure.header;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class HeaderViewModel {

    private final List<HeaderItemViewModel> items;
    private final boolean hasContent;
}
