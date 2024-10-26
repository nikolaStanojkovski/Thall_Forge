package com.musicdistribution.thallforge.components.structure.footer;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class FooterItemViewModel {

    private final String title;
    private final String link;
}
