package com.musicdistribution.thallcore.components.shared.audio;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class AudioViewModel {

    private final boolean hasContent;
}
