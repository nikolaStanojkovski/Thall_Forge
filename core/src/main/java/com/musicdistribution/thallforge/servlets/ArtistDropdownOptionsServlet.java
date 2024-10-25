package com.musicdistribution.thallforge.servlets;

import com.drew.lang.annotations.NotNull;
import com.google.gson.Gson;
import com.musicdistribution.thallforge.constants.ThallforgeConstants;
import com.musicdistribution.thallforge.services.ArtistPersistService;
import com.musicdistribution.thallforge.servlets.models.ArtistDropdownOptionViewModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component(service = {Servlet.class})
@SlingServletPaths(ThallforgeConstants.Endpoints.ARTIST_DROPDOWN_OPTIONS_ENDPOINT_PATH)
public class ArtistDropdownOptionsServlet extends SlingSafeMethodsServlet {

    @Reference
    private ArtistPersistService artistPersistService;

    @Override
    protected void doGet(@NotNull SlingHttpServletRequest request,
                         @NotNull SlingHttpServletResponse response) {
        try {
            response.getOutputStream().print(getArtists());
        } catch (IOException e) {
            log.error("Could not artists for the dropdown-options", e);
        }
    }

    private String getArtists() {
        List<ArtistDropdownOptionViewModel> artists = artistPersistService.getAvailableArtists().stream()
                .map(artist -> ArtistDropdownOptionViewModel.builder()
                        .value(artist.getPath())
                        .text(artist.getName())
                        .build())
                .collect(Collectors.toList());
        return new Gson().toJson(artists);
    }
}
