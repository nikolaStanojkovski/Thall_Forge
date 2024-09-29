package com.musicdistribution.thallcore.components.content.musicplayer;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public interface MusicPlayerTrackResourceModel {

    @ValueMapValue
    String getTitle();

    @ValueMapValue
    String getArtist();

    @ChildResource
    Resource getAudioTrack();

    @ChildResource
    Resource getTrackCover();
}
