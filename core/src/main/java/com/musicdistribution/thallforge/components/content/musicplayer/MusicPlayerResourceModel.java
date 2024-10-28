package com.musicdistribution.thallforge.components.content.musicplayer;

import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.api.resource.Resource;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public interface MusicPlayerResourceModel {

    @ChildResource
    MusicPlayerTrackResourceModel getTrackInfo();

    @ValueMapValue
    @Default(values = "Play")
    String getPlayLabel();

    @ValueMapValue
    @Default(values = "Stop")
    String getStopLabel();

    @ValueMapValue
    @Default(values = "Volume")
    String getVolumeControlLabel();

    @ValueMapValue
    @Default(values = "Your browser does not support the audio element.")
    String getBrowserSupportErrorMessage();

    @ValueMapValue
    @Default(values = "Repeat")
    String getRepeatLabel();

    @ValueMapValue
    @Default(booleanValues = false)
    boolean isEnableShuffle();

    @ValueMapValue
    String getShuffleAlbumPath();

    @ValueMapValue
    @Default(values = "Shuffle")
    String getShuffleLabel();
}
