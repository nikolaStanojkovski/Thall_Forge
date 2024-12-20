package com.musicdistribution.thallforge.components.content.genreexplorer;

import com.musicdistribution.thallforge.services.AlbumQueryService;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;

@Model(adaptables = Resource.class)
public class GenreExplorerController {

    @Self
    private Resource resource;

    @OSGiService
    private ResourceResolverRetrievalService resourceResolverRetrievalService;

    @OSGiService
    private AlbumQueryService albumQueryService;

    @Getter
    private GenreExplorerViewModel model;

    @PostConstruct
    private void init() {
        model = GenreExplorerViewModelProvider.builder()
                .resource(resource)
                .albumQueryService(albumQueryService)
                .resourceResolverRetrievalService(resourceResolverRetrievalService)
                .build()
                .getViewModel();
    }
}
