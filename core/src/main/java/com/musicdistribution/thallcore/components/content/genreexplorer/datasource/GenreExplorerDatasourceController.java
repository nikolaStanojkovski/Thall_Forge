package com.musicdistribution.thallcore.components.content.genreexplorer.datasource;

import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.musicdistribution.thallcore.components.shared.genre.Genre;
import com.musicdistribution.thallcore.services.ResourceResolverRetrievalService;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

@Model(adaptables = SlingHttpServletRequest.class)
public class GenreExplorerDatasourceController {

    @Self
    private SlingHttpServletRequest request;

    @OSGiService
    private ResourceResolverRetrievalService resourceResolverRetrievalService;

    @Getter
    private SimpleDataSource genreDataSource;

    @PostConstruct
    protected void init() {
        this.genreDataSource = resourceResolverRetrievalService.getAdministrativeResourceResolver()
                .map(this::createGenreResources)
                .map(SimpleDataSource::new)
                .orElse(null);
    }

    private Iterator<Resource> createGenreResources(ResourceResolver resourceResolver) {
        return Arrays.stream(Genre.values())
                .map(genre -> createResource(resourceResolver, genre))
                .iterator();
    }

    private Resource createResource(ResourceResolver resourceResolver, Genre genre) {
        return new ValueMapResource(resourceResolver,
                StringUtils.EMPTY,
                StringUtils.EMPTY,
                new ValueMapDecorator(Map.of(
                        "text", genre.getTitle(),
                        "value", genre.getName()
                )));
    }

}
