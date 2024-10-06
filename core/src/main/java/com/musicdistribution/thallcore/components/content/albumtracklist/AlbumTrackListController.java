package com.musicdistribution.thallcore.components.content.albumtracklist;

import com.musicdistribution.thallcore.services.AlbumTrackListService;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;

@Model(adaptables = SlingHttpServletRequest.class)
public class AlbumTrackListController {

    @Self
    private SlingHttpServletRequest request;

    @SlingObject
    private Resource resource;

    @OSGiService
    private AlbumTrackListService albumTrackListService;

    @Getter
    private AlbumTrackListViewModel model;

    @PostConstruct
    private void init() {
        model = AlbumTrackListViewModelProvider.builder()
                .request(request)
                .resource(resource)
                .albumTrackListService(albumTrackListService)
                .build()
                .getViewModel();
    }
}
