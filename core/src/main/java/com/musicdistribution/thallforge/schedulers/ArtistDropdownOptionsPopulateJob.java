package com.musicdistribution.thallforge.schedulers;

import com.google.gson.Gson;
import com.musicdistribution.thallforge.constants.ThallforgeConstants;
import com.musicdistribution.thallforge.schedulers.models.ArtistDropdownOptionViewModel;
import com.musicdistribution.thallforge.schedulers.models.ArtistDropdownOptionsViewModel;
import com.musicdistribution.thallforge.services.ArtistPersistService;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.ResourceResolver;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
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
        Optional.ofNullable(resourceResolver.getResource(ThallforgeConstants.Paths.ARTIST_DROPDOWN_OPTIONS))
                .map(r -> r.getChild("jcr:content"))
                .map(fileResource -> fileResource.adaptTo(ModifiableValueMap.class))
                .ifPresent(modifiableValueMap -> {
                    try {
                        byte[] artistsListBytes = getArtists().getBytes(StandardCharsets.UTF_8);
                        InputStream artistsInputStream = new ByteArrayInputStream(artistsListBytes);
                        Session session = resourceResolver.adaptTo(Session.class);
                        if (session != null) {
                            modifiableValueMap.put("jcr:data", session.getValueFactory().createBinary(artistsInputStream));
                            resourceResolver.commit();
                        } else {
                            log.error("Could not read the administrative resource resolver");
                        }
                    } catch (PersistenceException | RepositoryException e) {
                        log.error("Could not update artist dropdown options data", e);
                    }
                });
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
