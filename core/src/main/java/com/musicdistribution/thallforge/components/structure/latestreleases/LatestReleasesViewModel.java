package com.musicdistribution.thallforge.components.structure.latestreleases;

import com.musicdistribution.thallforge.components.shared.audio.AudioViewModel;
import com.musicdistribution.thallforge.services.impl.models.AlbumViewModel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class LatestReleasesViewModel {

    private final String title;
    private final List<AlbumViewModel> albums;
    private final boolean hasContent;
}
