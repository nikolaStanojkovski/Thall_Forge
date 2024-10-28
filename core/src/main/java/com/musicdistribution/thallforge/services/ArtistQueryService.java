package com.musicdistribution.thallforge.services;

import com.musicdistribution.thallforge.components.shared.artist.ArtistContentFragmentViewModel;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.List;
import java.util.Optional;

public interface ArtistQueryService {

    List<ArtistContentFragmentViewModel> getAvailableArtists(ResourceResolver resourceResolver);

    Optional<ArtistContentFragmentViewModel> getArtistData(ResourceResolver resourceResolver, String artistReference);
}
