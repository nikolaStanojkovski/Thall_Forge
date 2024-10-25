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
                .map(fileResource -> fileResource.adaptTo(ModifiableValueMap.class))
                .ifPresent(modifiableValueMap -> {
                    try {
                        modifiableValueMap.put("jcr:data", getArtists().getBytes(StandardCharsets.UTF_8));
                        modifiableValueMap.put("jcr:mimeType", "application/json");
                        resourceResolver.commit();
                    } catch (PersistenceException e) {
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
