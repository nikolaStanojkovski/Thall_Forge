package com.musicdistribution.thallcore.components.content.genreexplorer;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class GenreExplorerAlbumViewModel {

    private final String id;
    private final String thumbnail;
    private final String title;
    private final String artist;
}
