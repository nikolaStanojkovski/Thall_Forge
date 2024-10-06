package com.musicdistribution.thallcore.services.impl;

import com.musicdistribution.thallcore.components.shared.audio.AudioViewModel;
import com.musicdistribution.thallcore.services.AlbumTrackListService;
import com.musicdistribution.thallcore.services.MusicPlayerShuffleService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.List;
import java.util.Optional;

@Component(service = MusicPlayerShuffleService.class)
public class MusicPlayerShuffleServiceImpl implements MusicPlayerShuffleService {

    @Reference
    private AlbumTrackListService albumTracklistService;

    @Override
    public Optional<AudioViewModel> shuffle(final String albumPath) {
        List<AudioViewModel> tracks = albumTracklistService.getTracks(albumPath);
        return getRandomSong(tracks);
    }

    private Optional<AudioViewModel> getRandomSong(List<AudioViewModel> tracks) {
        int randomSongIndex = generateRandomIndex(tracks.size());
        return Optional.ofNullable(tracks.get(randomSongIndex));
    }

    private int generateRandomIndex(int albumLength) {
        return (int) (Math.random() * (albumLength - 1));
    }
}
