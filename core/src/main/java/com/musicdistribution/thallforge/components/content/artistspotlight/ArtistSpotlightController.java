package com.musicdistribution.thallforge.components.content.artistspotlight;

import com.musicdistribution.thallforge.services.AlbumQueryService;
import com.musicdistribution.thallforge.services.ArtistQueryService;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;

@Model(adaptables = Resource.class)
public class ArtistSpotlightController {

    @Self
    private Resource resource;

    @OSGiService
    private ResourceResolverRetrievalService resourceResolverRetrievalService;

    @OSGiService
    private AlbumQueryService albumQueryService;

    @OSGiService
    private ArtistQueryService artistQueryService;

    @Getter
    private ArtistSpotlightViewModel model;

    @PostConstruct
    private void init() {
        model = ArtistSpotlightViewModelProvider.builder()
                .resource(resource)
                .resourceResolverRetrievalService(resourceResolverRetrievalService)
                .albumQueryService(albumQueryService)
                .artistQueryService(artistQueryService)
                .build()
                .getViewModel();
    }
}
