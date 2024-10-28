package com.musicdistribution.thallforge.components.structure.header;

import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;

@Model(adaptables = Resource.class)
public class HeaderController {

    @Self
    private Resource resource;

    @OSGiService
    private ResourceResolverRetrievalService resourceResolverRetrievalService;

    @Getter
    private HeaderViewModel model;

    @PostConstruct
    private void init() {
        model = HeaderViewModelProvider.builder()
                .resource(resource)
                .resourceResolverRetrievalService(resourceResolverRetrievalService)
                .build()
                .getViewModel();
    }
}
