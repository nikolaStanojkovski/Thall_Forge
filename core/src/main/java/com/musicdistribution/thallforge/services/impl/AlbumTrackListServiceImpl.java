package com.musicdistribution.thallforge.services.impl;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.commons.util.DamUtil;
import com.musicdistribution.thallforge.components.shared.audio.AudioViewModel;
import com.musicdistribution.thallforge.components.shared.audio.AudioViewModelProvider;
import com.musicdistribution.thallforge.services.AlbumTrackListService;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Arrays;
import java.util.Collections;
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
        if (StringUtils.isBlank(albumPath)) {
            return Collections.emptyList();
        }
        return resourceResolverRetrievalService.getContentDamResourceResolver()
                .map(resourceResolver -> Optional.ofNullable(resourceResolver.getResource(albumPath))
                        .map(this::getTracklistStream)
                        .orElse(Stream.empty())
                        .filter(songResource -> isAudioAsset(songResource, resourceResolver))
                        .map(this::getSongViewModel)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());

    }

    private boolean isAudioAsset(Resource songResource, ResourceResolver resourceResolver) {
        if (DamUtil.isAsset(songResource)) {
            return false;
        }
        return Optional.ofNullable(songResource.adaptTo(Asset.class))
                .map(this::isAudioAsset)
                .orElse(false);
    }

    private boolean isAudioAsset(Asset asset) {
        List<String> audioMimeTypes = Arrays.asList("audio/aac", "audio/mpeg", "audio/ogg", "audio/wav");
        String mimeType = asset.getMimeType();
        if (StringUtils.isBlank(mimeType)) {
            return false;
        }
        return audioMimeTypes.contains(mimeType);
    }

    private Stream<Resource> getTracklistStream(Resource albumResource) {
        return StreamSupport.stream(albumResource.getChildren().spliterator(), false);

    }

    private AudioViewModel getSongViewModel(Resource songResource) {
        return AudioViewModelProvider.builder()
                .audioResource(songResource)
                .build()
                .getViewModel();
    }
}
