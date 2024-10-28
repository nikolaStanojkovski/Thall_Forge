package com.musicdistribution.thallforge.components.structure.footer;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class FooterViewModel {

    private final List<FooterItemViewModel> items;
    private final boolean hasContent;
}
