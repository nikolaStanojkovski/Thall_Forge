package com.musicdistribution.thallcore.components.content.musicplayer;

import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;

@Model(adaptables = SlingHttpServletRequest.class)
public class MusicPlayerController {

    @Self
    private SlingHttpServletRequest request;

    @SlingObject
    private Resource resource;

    @SlingObject
    private ResourceResolver resourceResolver;

    @Getter
    private MusicPlayerViewModel model;

    @PostConstruct
    private void init() {
        model = MusicPlayerViewModelProvider.builder()
                .request(request)
                .resource(resource)
                .resourceResolver(resourceResolver)
                .build()
                .getViewModel();
    }
}
