package com.musicdistribution.thallforge.services;

import com.musicdistribution.thallforge.components.shared.audio.AudioViewModel;

import java.util.List;

public interface AlbumTrackListService {

    List<AudioViewModel> getTracks(String albumPath);
}
