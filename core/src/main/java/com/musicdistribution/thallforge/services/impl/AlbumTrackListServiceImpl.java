package com.musicdistribution.thallforge.services.impl;

import com.musicdistribution.thallforge.components.shared.audio.AudioViewModel;
import com.musicdistribution.thallforge.components.shared.audio.AudioViewModelProvider;
import com.musicdistribution.thallforge.services.AlbumTrackListService;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component(service = AlbumTrackListService.class)
public class AlbumTrackListServiceImpl implements AlbumTrackListService {

    @Reference
    private ResourceResolverRetrievalService resourceResolverRetrievalService;

    @Override
    public List<AudioViewModel> getTracks(final String albumPath) {
        return Optional.ofNullable(albumPath)
                .filter(StringUtils::isNotBlank)
                .map(this::getTracklistStream)
                .orElse(Stream.empty())
                .map(this::getSongViewModel)
                .collect(Collectors.toList());
    }

    private Stream<Resource> getTracklistStream(String albumPath) {
        return resourceResolverRetrievalService.getAdministrativeResourceResolver()
                .map(resourceResolver -> resourceResolver.getResource(albumPath))
                .map(Resource::getChildren)
                .map(trackList -> StreamSupport.stream(trackList.spliterator(), false))
                .orElse(Stream.empty());

    }

    private AudioViewModel getSongViewModel(Resource songResource) {
        return AudioViewModelProvider.builder()
                .audioResource(songResource)
                .build()
                .getViewModel();
    }
}
