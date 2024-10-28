package com.musicdistribution.thallforge.components.content.genreexplorer;

import com.musicdistribution.thallforge.components.shared.audio.AudioViewModel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
public class GenreExplorerAlbumSongsViewModel {

    private final String albumId;
    private final List<AudioViewModel> songs;
}
