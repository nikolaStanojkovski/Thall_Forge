package com.musicdistribution.thallforge.components.content.genreexplorer;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class GenreExplorerViewModel {

    private final String selectedGenre;
    private final List<GenreExplorerAlbumViewModel> albums;
    private final List<GenreExplorerAlbumSongsViewModel> albumSongs;
    private final boolean hasContent;
}
