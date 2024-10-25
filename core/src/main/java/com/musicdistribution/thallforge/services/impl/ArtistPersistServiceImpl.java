package com.musicdistribution.thallforge.services.impl;

import com.musicdistribution.thallforge.components.contentfragments.artist.ArtistContentFragmentResourceModel;
import com.musicdistribution.thallforge.components.contentfragments.artist.ArtistContentFragmentViewModel;
import com.musicdistribution.thallforge.components.contentfragments.artist.ArtistContentFragmentViewModelProvider;
import com.musicdistribution.thallforge.constants.ThallforgeConstants;
import com.musicdistribution.thallforge.services.ArtistPersistService;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import com.musicdistribution.thallforge.utils.ResourceUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Reference;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ArtistPersistServiceImpl implements ArtistPersistService {

    @Reference
    private ResourceResolverRetrievalService resourceResolverRetrievalService;

    @Override
    public List<ArtistContentFragmentViewModel> getAvailableArtists() {
        return resourceResolverRetrievalService.getContentDamResourceResolver()
                .map(this::getArtists)
                .orElse(Collections.emptyList());
    }

    private List<ArtistContentFragmentViewModel> getArtists(ResourceResolver resourceResolver) {
        return Optional.ofNullable(resourceResolver.getResource(ThallforgeConstants.Paths.CONTENT_DAM))
                .map(ResourceUtils::getAllChildren)
                .map(childrenStream -> childrenStream
                        .map(childResource -> getArtist(resourceResolver, childResource))
                        .collect(Collectors.toList())
                ).orElse(Collections.emptyList());
    }

    private ArtistContentFragmentViewModel getArtist(ResourceResolver resourceResolver,
                                                     Resource childResource) {
        return ArtistContentFragmentViewModelProvider.builder()
                .resolver(resourceResolver)
                .artistContentFragmentResource(childResource)
                .build().getViewModel();
    }
}
