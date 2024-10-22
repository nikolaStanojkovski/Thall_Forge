package com.musicdistribution.thallforge.components.content.genreexplorer.datasource;

import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.musicdistribution.thallforge.components.shared.genre.Genre;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
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
import java.util.Map;

@Model(adaptables = SlingHttpServletRequest.class)
public class GenreExplorerDatasourceController {

    @Self
    private SlingHttpServletRequest request;

    @OSGiService
    private ResourceResolverRetrievalService resourceResolverRetrievalService;

    @PostConstruct
    protected void init() {
        request.setAttribute(DataSource.class.getName(), getGenreDataSource());
    }

    private DataSource getGenreDataSource() {
        return resourceResolverRetrievalService.getAdministrativeResourceResolver()
                .map(resourceResolver -> Arrays.stream(Genre.values())
                        .map(genre -> createResource(resourceResolver, genre))
                        .iterator())
                .map(SimpleDataSource::new)
                .orElse(null);
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
