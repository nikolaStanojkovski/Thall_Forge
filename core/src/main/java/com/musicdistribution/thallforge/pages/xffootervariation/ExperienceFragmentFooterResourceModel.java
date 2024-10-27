package com.musicdistribution.thallforge.pages.xffootervariation;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.Calendar;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public interface ExperienceFragmentFooterResourceModel {

    @ValueMapValue
    String getFooterExperienceFragmentReference();
}
