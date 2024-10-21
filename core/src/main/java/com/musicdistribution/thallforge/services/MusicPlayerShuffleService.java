package com.musicdistribution.thallforge.services;

import com.musicdistribution.thallforge.components.shared.audio.AudioViewModel;

import java.util.Optional;

public interface MusicPlayerShuffleService {

    Optional<AudioViewModel> shuffle(String albumPath);
}
