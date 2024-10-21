package com.musicdistribution.thallforge.components.shared.image;

import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.osgi.resource.Resource;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public interface ImageResourceModel {

    @ValueMapValue
    String getAlt();

    @ValueMapValue
    String getCaption();

    @ValueMapValue
    String getFileName();

    @ValueMapValue
    String getFileReference();
}
