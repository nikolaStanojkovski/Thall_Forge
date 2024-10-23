package com.musicdistribution.thallforge.services;

import com.musicdistribution.thallforge.services.impl.models.ShuffleSongViewModel;

import java.util.Optional;

public interface MusicPlayerShuffleService {

    Optional<ShuffleSongViewModel> shuffle(String albumPath);
}
