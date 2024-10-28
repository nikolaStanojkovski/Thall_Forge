package com.musicdistribution.thallforge.components.content.genreexplorer;

import com.musicdistribution.thallforge.services.impl.models.AlbumViewModel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class GenreExplorerViewModel {

    private final String selectedGenre;
    private final List<AlbumViewModel> albums;
    private final List<GenreExplorerAlbumSongsViewModel> albumSongs;
    private final boolean hasContent;
}
