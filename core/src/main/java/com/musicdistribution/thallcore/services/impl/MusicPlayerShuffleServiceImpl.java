package com.musicdistribution.thallcore.services.impl;

import com.musicdistribution.thallcore.components.shared.audio.AudioViewModel;
import com.musicdistribution.thallcore.components.shared.audio.AudioViewModelProvider;
import com.musicdistribution.thallcore.services.MusicPlayerShuffleService;
import com.musicdistribution.thallcore.services.ResourceResolverRetrievalService;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.sling.api.resource.Resource;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.List;
import java.util.Optional;

@Component(service = MusicPlayerShuffleService.class)
public class MusicPlayerShuffleServiceImpl implements MusicPlayerShuffleService {

    @Reference
    private ResourceResolverRetrievalService resourceResolverRetrievalService;

    @Override
    public Optional<AudioViewModel> shuffle(final String albumPath) {
        return resourceResolverRetrievalService.getAdministrativeResourceResolver()
                .map(resourceResolver -> resourceResolver.getResource(albumPath))
                .map(albumAsset -> albumAsset.getChildren().iterator())
                .map(IteratorUtils::toList)
                .flatMap(this::getRandomSong)
                .map(this::getSongViewModel);
    }

    private AudioViewModel getSongViewModel(Resource songResource) {
        return AudioViewModelProvider.builder()
                .audioResource(songResource)
                .build()
                .getViewModel();
    }

    private Optional<Resource> getRandomSong(List<Resource> songs) {
        int randomSongIndex = generateRandomIndex(songs.size());
        return Optional.ofNullable(songs.get(randomSongIndex));
    }

    private int generateRandomIndex(int albumLength) {
        return (int) (Math.random() * (albumLength - 1));
    }
}
