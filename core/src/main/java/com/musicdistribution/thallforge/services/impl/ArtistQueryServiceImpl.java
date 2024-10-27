package com.musicdistribution.thallforge.services.impl;

import com.musicdistribution.thallforge.components.shared.artist.ArtistContentFragmentViewModel;
import com.musicdistribution.thallforge.components.shared.artist.ArtistContentFragmentViewModelProvider;
import com.musicdistribution.thallforge.constants.ThallforgeConstants;
import com.musicdistribution.thallforge.services.ArtistQueryService;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import com.musicdistribution.thallforge.utils.ResourceUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component(service = ArtistQueryService.class)
public class ArtistQueryServiceImpl implements ArtistQueryService {

    @Reference
    private ResourceResolverRetrievalService resourceResolverRetrievalService;

    @Override
    public List<ArtistContentFragmentViewModel> getAvailableArtists(ResourceResolver resourceResolver) {
        return Optional.ofNullable(resourceResolver.getResource(ThallforgeConstants.Paths.CONTENT_DAM))
                .map(ResourceUtils::getAllChildren)
                .map(childrenStream -> childrenStream
                        .map(childResource -> getArtist(resourceResolver, childResource))
                        .filter(ArtistContentFragmentViewModel::isHasContent)
                        .collect(Collectors.toList())
                ).orElse(Collections.emptyList());
    }

    @Override
    public Optional<ArtistContentFragmentViewModel> getArtistData(ResourceResolver resourceResolver, String artistReference) {
        return Optional.ofNullable(resourceResolver.getResource(artistReference))
                .map(artistContentFragmentResource -> getArtist(resourceResolver, artistContentFragmentResource))
                .filter(ArtistContentFragmentViewModel::isHasContent);
    }

    private ArtistContentFragmentViewModel getArtist(ResourceResolver resourceResolver,
                                                     Resource childResource) {
        return ArtistContentFragmentViewModelProvider.builder()
                .resolver(resourceResolver)
                .artistContentFragmentResource(childResource)
                .build().getViewModel();
    }
}
