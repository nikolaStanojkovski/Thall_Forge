package com.musicdistribution.thallforge.components.structure.topfansbanner;

import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;

@Model(adaptables = SlingHttpServletRequest.class)
public class TopFansBannerController {

    @Self
    private SlingHttpServletRequest request;

    @Getter
    private TopFansBannerViewModel model;

    @PostConstruct
    private void init() {
        model = TopFansBannerViewModelProvider.builder()
                .request(request)
                .build()
                .getViewModel();
    }
}
