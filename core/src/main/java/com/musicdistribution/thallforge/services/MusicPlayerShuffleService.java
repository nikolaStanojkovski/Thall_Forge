package com.musicdistribution.thallforge.services;

import com.musicdistribution.thallforge.services.impl.models.ShuffleSongViewModel;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.Optional;

public interface MusicPlayerShuffleService {

    Optional<ShuffleSongViewModel> shuffle(ResourceResolver resourceResolver, String albumPath);
}
