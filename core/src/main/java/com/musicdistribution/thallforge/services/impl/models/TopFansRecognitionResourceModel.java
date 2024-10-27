package com.musicdistribution.thallforge.services.impl.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public interface TopFansRecognitionResourceModel {

    @ValueMapValue
    Long getTopSiteInteractionsNumber();
}
