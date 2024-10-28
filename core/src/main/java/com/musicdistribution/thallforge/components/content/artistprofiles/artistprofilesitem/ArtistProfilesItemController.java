package com.musicdistribution.thallforge.components.content.artistprofiles.artistprofilesitem;

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
public class ArtistProfilesItemController {

    @Self
    private Resource resource;

    @OSGiService
    private ArtistQueryService artistQueryService;

    @OSGiService
    private ResourceResolverRetrievalService resourceResolverRetrievalService;

    @Getter
    private ArtistProfilesItemViewModel model;

    @PostConstruct
    private void init() {
        model = ArtistProfilesItemViewModelProvider.builder()
                .resource(resource)
                .artistQueryService(artistQueryService)
                .resourceResolverRetrievalService(resourceResolverRetrievalService)
                .build()
                .getViewModel();
    }
}
