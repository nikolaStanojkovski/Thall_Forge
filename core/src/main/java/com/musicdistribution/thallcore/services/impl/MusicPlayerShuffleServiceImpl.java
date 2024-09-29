package com.musicdistribution.thallcore.services.impl;

import com.musicdistribution.thallcore.components.shared.audio.AudioViewModel;
import com.musicdistribution.thallcore.services.MusicPlayerShuffleService;
import org.osgi.service.component.annotations.Component;

import java.util.Optional;

@Component(service = MusicPlayerShuffleService.class)
public class MusicPlayerShuffleServiceImpl implements MusicPlayerShuffleService {
    @Override
    public Optional<AudioViewModel> shuffle(final String albumPath) {
        return Optional.empty();
    }
}
