package com.musicdistribution.thallforge.pages.xfheadervariation;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public interface ExperienceFragmentHeaderResourceModel {

    @ValueMapValue
    String getHeaderExperienceFragmentReference();
}
