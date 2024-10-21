package com.musicdistribution.thallforge.components.content.genreexplorer;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class GenreExplorerAlbumViewModel {

    private final String id;
    private final String link;
    private final String thumbnail;
    private final String title;
    private final String artist;
}
