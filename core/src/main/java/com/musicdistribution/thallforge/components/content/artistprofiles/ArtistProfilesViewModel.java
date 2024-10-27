package com.musicdistribution.thallforge.components.content.artistprofiles;

import com.musicdistribution.thallforge.components.shared.audio.AudioViewModel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class ArtistProfilesViewModel {

    private final String title;
    private final String thumbnail;
    private final List<AudioViewModel> tracks;
    private final String downloadLabel;
    private final boolean hasContent;
}
