package com.musicdistribution.thallforge.components.shared.audio;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class AudioViewModel {

    private final String title;
    private final String link;
    private final AudioDurationViewModel duration;
    private final boolean hasContent;
}
