package com.musicdistribution.thallcore.components.content.genreexplorer;

import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.osgi.resource.Resource;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public interface GenreExplorerResourceModel {

    @ValueMapValue
    String getGenre();

    @ValueMapValue
    @Default(intValues = 10)
    int getLimit();

    @ValueMapValue
    @Default(booleanValues = false)
    boolean isSort();
}
