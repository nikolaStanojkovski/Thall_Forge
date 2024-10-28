package com.musicdistribution.thallforge.components.content.artistspotlight;

import com.musicdistribution.thallforge.services.impl.models.AlbumViewModel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class ArtistSpotlightViewModel {

    private final String artistImage;
    private final String artistName;
    private final String artistBiography;
    private final List<AlbumViewModel> albums;
    private final boolean hasContent;
}
