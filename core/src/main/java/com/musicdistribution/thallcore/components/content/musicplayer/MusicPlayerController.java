package com.musicdistribution.thallcore.components.content.musicplayer;

import com.musicdistribution.thallcore.services.ResourceResolverRetrievalService;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;

@Model(adaptables = Resource.class)
public class MusicPlayerController {

    @Self
    private Resource resource;

    @OSGiService
    private ResourceResolverRetrievalService resourceResolverRetrievalService;

    @Getter
    private MusicPlayerViewModel model;

    @PostConstruct
    private void init() {
        model = MusicPlayerViewModelProvider.builder()
                .resource(resource)
                .resourceResolverRetrievalService(resourceResolverRetrievalService)
                .build()
                .getViewModel();
    }
}
