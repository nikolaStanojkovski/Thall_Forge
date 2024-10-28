package com.musicdistribution.thallforge.components.structure.experiencefragments.xfheader;

import com.day.cq.wcm.api.Page;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;

@Model(adaptables = SlingHttpServletRequest.class)
public class ExperienceFragmentHeaderController {

    @Self
    private SlingHttpServletRequest request;

    @ScriptVariable
    private Page currentPage;

    @OSGiService
    private ResourceResolverRetrievalService resourceResolverRetrievalService;

    @Getter
    private ExperienceFragmentHeaderViewModel model;

    @PostConstruct
    private void init() {
        model = ExperienceFragmentHeaderViewModelProvider.builder()
                .currentPage(currentPage)
                .resourceResolverRetrievalService(resourceResolverRetrievalService)
                .build()
                .getViewModel();
    }
}
