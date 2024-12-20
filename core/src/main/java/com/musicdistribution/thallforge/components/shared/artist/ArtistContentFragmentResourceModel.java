package com.musicdistribution.thallforge.components.shared.artist;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public interface ArtistContentFragmentResourceModel {

    @ValueMapValue
    String getBiography();

    @ValueMapValue
    String getImage();

    @ValueMapValue
    String getName();

    @ValueMapValue
    Integer getRating();
}
