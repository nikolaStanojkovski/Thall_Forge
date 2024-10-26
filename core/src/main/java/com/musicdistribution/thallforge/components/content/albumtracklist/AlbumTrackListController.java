package com.musicdistribution.thallforge.components.content.albumtracklist;

import com.musicdistribution.thallforge.services.AlbumTrackListService;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;

@Model(adaptables = Resource.class)
public class AlbumTrackListController {

    @Self
    private Resource resource;

    @OSGiService
    private AlbumTrackListService albumTrackListService;

    @OSGiService
    private ResourceResolverRetrievalService resourceResolverRetrievalService;

    @Getter
    private AlbumTrackListViewModel model;

    @PostConstruct
    private void init() {
        model = AlbumTrackListViewModelProvider.builder()
                .resource(resource)
                .albumTrackListService(albumTrackListService)
                .resourceResolverRetrievalService(resourceResolverRetrievalService)
                .build()
                .getViewModel();
    }
}
