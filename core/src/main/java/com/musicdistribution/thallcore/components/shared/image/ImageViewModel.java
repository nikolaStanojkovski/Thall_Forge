package com.musicdistribution.thallcore.components.shared.image;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class ImageViewModel {

    private final boolean hasContent;
}
