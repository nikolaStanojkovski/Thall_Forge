package com.musicdistribution.thallforge.components.content.artistspotlight;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public interface ArtistSpotlightResourceModel {

    @ValueMapValue
    String getGenre();

    @ValueMapValue
    @Default(intValues = 10)
    int getLimit();

    @ValueMapValue
    @Default(booleanValues = false)
    boolean isSort();
}
