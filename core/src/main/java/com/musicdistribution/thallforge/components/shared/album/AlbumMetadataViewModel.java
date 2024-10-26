package com.musicdistribution.thallforge.components.shared.album;

import com.musicdistribution.thallforge.components.shared.artist.ArtistContentFragmentViewModel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class AlbumMetadataViewModel {

    private final String title;
    private final String genre;
    private final ArtistContentFragmentViewModel artist;
    private final String date;
    private final String thumbnail;
}
