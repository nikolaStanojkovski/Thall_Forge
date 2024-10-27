package com.musicdistribution.thallforge.pages.xfheadervariation;

import com.day.cq.wcm.api.Page;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;

import javax.annotation.PostConstruct;

@Model(adaptables = SlingHttpServletRequest.class)
public class ExperienceFragmentHeaderController {

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
