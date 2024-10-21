package com.musicdistribution.thallforge.components.shared.image;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class ImageViewModel {

    private final String link;
    private final String alt;
    private final String caption;
    private final boolean hasContent;
}
