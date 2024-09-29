package com.musicdistribution.thallcore.components.shared.image;

import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.osgi.resource.Resource;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public interface ImageResourceModel {

    @ValueMapValue
    String getFileName();

    @ValueMapValue
    String getFileReference();
}
