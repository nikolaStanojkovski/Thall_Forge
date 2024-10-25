package com.musicdistribution.thallforge.schedulers;

import com.google.gson.Gson;
import com.musicdistribution.thallforge.constants.ThallforgeConstants;
import com.musicdistribution.thallforge.schedulers.models.ArtistDropdownOptionViewModel;
import com.musicdistribution.thallforge.schedulers.models.ArtistDropdownOptionsViewModel;
import com.musicdistribution.thallforge.services.ArtistPersistService;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class ArtistDropdownOptionsPopulateJob implements Runnable {

    private final ResourceResolverRetrievalService resourceResolverRetrievalService;
    private final ArtistPersistService artistPersistService;

    @Override
    public void run() {
        resourceResolverRetrievalService.getAdministrativeResourceResolver()
                .ifPresent(this::updateArtistDropdownOptionsData);
    }

    private void updateArtistDropdownOptionsData(ResourceResolver resourceResolver) {
        Optional.ofNullable(resourceResolver.getResource(ThallforgeConstants.Paths.ETC_THALLFORGE))
                .ifPresent(parentResource -> {
                    try {
                        removeCurrentResource(resourceResolver, parentResource);
                        createArtistDropdownOptionsData(parentResource, resourceResolver);
                    } catch (PersistenceException e) {
                        log.error("Could not update artist dropdown options data", e);
                    }
                });
    }

    private void removeCurrentResource(ResourceResolver resourceResolver,
                                       Resource parentResource) throws PersistenceException {
        String resourcePath = String.format("%s/%s.%s", parentResource.getPath(),
                ThallforgeConstants.Names.ARTIST_DROPDOWN_OPTIONS,
                ThallforgeConstants.Extensions.JSON);
        Optional.ofNullable(resourceResolver.getResource(resourcePath))
                .ifPresent(r -> {
                    try {
                        resourceResolver.delete(r);
                        resourceResolver.commit();
                    } catch (PersistenceException e) {
                        log.error("Could not remove resource", e);
                    }
                });
    }

    private void createArtistDropdownOptionsData(Resource parentResource,
                                                 ResourceResolver resourceResolver) throws PersistenceException {
        byte[] artistsByteStream = getArtists().getBytes(StandardCharsets.UTF_8);

        Map<String, Object> fileProperties = new HashMap<>();
        fileProperties.put("jcr:primaryType", "nt:file");

        Map<String, Object> contentProperties = new HashMap<>();
        contentProperties.put("jcr:primaryType", "nt:resource");
        contentProperties.put("jcr:data", artistsByteStream);
        contentProperties.put("jcr:mimeType", "application/json");
        contentProperties.put("jcr:lastModified", System.currentTimeMillis());

        Resource fileResource = resourceResolver.create(parentResource, "artistDropdownOptions.json", fileProperties);
        resourceResolver.create(fileResource, "jcr:content", contentProperties);
        resourceResolver.commit();
    }

    private String getArtists() {
        List<ArtistDropdownOptionViewModel> artists = artistPersistService.getAvailableArtists().stream()
                .map(artist -> ArtistDropdownOptionViewModel.builder()
                        .text(artist.getName())
                        .value(artist.getPath())
                        .build())
                .collect(Collectors.toList());
        return new Gson().toJson(ArtistDropdownOptionsViewModel.builder()
                .options(artists)
                .build());
    }
}
