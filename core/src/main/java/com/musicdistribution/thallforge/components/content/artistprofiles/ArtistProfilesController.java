package com.musicdistribution.thallforge.components.content.artistprofiles;

import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;

@Model(adaptables = Resource.class)
public class ArtistProfilesController {

    @Self
    private Resource resource;

    @Getter
    private ArtistProfilesViewModel model;

    @PostConstruct
    private void init() {
        model = ArtistProfilesViewModelProvider.builder()
                .resource(resource)
                .build()
                .getViewModel();
    }
}
