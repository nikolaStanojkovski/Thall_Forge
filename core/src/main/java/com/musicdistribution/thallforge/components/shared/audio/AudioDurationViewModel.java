package com.musicdistribution.thallforge.components.shared.audio;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class AudioDurationViewModel {

    private final Integer minutes;
    private final Integer seconds;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (minutes < 10) {
            sb.append("0");
        }
        sb.append(minutes).append(":");
        if (seconds < 10) {
            sb.append("0");
        }
        sb.append(seconds);
        return sb.toString();
    }
}
