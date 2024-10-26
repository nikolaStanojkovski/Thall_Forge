package com.musicdistribution.thallforge.services;

import com.musicdistribution.thallforge.components.shared.artist.ArtistContentFragmentViewModel;

import java.util.List;

public interface ArtistQueryService {

    List<ArtistContentFragmentViewModel> getAvailableArtists();
}
