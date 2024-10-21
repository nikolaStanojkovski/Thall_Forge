package com.musicdistribution.thallforge.components.shared.audio;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public interface AudioResourceModel {

    @ValueMapValue
    String getFileName();

    @ValueMapValue
    String getFileReference();

    @ValueMapValue(name = "dc:duration")
    String getDuration();
}
