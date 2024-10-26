package com.musicdistribution.thallforge.services.impl.models;

import com.musicdistribution.thallforge.components.shared.audio.AudioViewModel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class AlbumViewModel {

    private final String id;
    private final String link;
    private final String thumbnail;
    private final String title;
    private final String artist;
    private final List<AudioViewModel> songs;
}
