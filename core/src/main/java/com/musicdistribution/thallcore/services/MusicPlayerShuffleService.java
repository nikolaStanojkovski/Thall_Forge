package com.musicdistribution.thallcore.services;

import com.musicdistribution.thallcore.components.shared.audio.AudioViewModel;

import java.util.Optional;

public interface MusicPlayerShuffleService {

    Optional<AudioViewModel> shuffle(String albumPath);
}
