package com.musicdistribution.thallforge.services;

import com.musicdistribution.thallforge.components.shared.audio.AudioViewModel;
import com.musicdistribution.thallforge.services.impl.models.AlbumViewModel;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.List;

public interface AlbumQueryService {

    List<AudioViewModel> getAlbumTracks(ResourceResolver resourceResolver, String albumPath);

    List<AlbumViewModel> getAlbums(ResourceResolver resourceResolver, String searchQuery, int limit);
}
