package com.musicdistribution.thallforge.components.structure.latestreleases;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public interface LatestReleasesResourceModel {

    @ValueMapValue
    @Default(values = "Download")
    String getDownloadLabel();

    @ValueMapValue
    String getAlbumPath();
}
