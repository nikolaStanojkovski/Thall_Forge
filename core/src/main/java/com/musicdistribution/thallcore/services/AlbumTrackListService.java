package com.musicdistribution.thallcore.services;

import com.musicdistribution.thallcore.components.shared.audio.AudioViewModel;

import java.util.List;

public interface AlbumTrackListService {

    List<AudioViewModel> getTracks(String albumPath);
}
