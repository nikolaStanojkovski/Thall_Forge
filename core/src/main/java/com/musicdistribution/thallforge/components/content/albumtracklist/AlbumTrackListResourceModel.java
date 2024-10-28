package com.musicdistribution.thallforge.components.content.albumtracklist;

import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.api.resource.Resource;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public interface AlbumTrackListResourceModel {

    @ValueMapValue
    @Default(values = "Download")
    String getDownloadLabel();

    @ValueMapValue
    String getAlbumPath();
}
