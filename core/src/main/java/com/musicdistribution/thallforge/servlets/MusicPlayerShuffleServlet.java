package com.musicdistribution.thallforge.servlets;

import com.drew.lang.annotations.NotNull;
import com.google.gson.Gson;
import com.musicdistribution.thallforge.components.content.musicplayer.MusicPlayerResourceModel;
import com.musicdistribution.thallforge.constants.ThallforgeConstants;
import com.musicdistribution.thallforge.services.MusicPlayerShuffleService;
import com.musicdistribution.thallforge.servlets.models.MusicPlayerShuffleViewModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component(service = {Servlet.class})
@SlingServletResourceTypes(
        resourceTypes = ThallforgeConstants.ResourceTypes.MUSIC_PLAYER,
        methods = HttpConstants.METHOD_GET,
        extensions = ThallforgeConstants.Extensions.JSON)
public class MusicPlayerShuffleServlet extends SlingSafeMethodsServlet {

    @Reference
    private MusicPlayerShuffleService shuffleService;

    @Override
    protected void doGet(@NotNull SlingHttpServletRequest request,
                         @NotNull SlingHttpServletResponse response) {
        try {
            Resource componentResource = request.getResource();
            String albumPath = getShuffleAlbumPath(componentResource);
            if (StringUtils.isBlank(albumPath)) {
                response.sendError(500);
            }
            response.getOutputStream().print(getSongJson(albumPath));
        } catch (IOException e) {
            log.error("Could not read the music-player album path", e);
        }
    }

    private String getSongJson(String albumPath) {
        String jsonString = shuffleService.shuffle(albumPath)
                .map(song -> MusicPlayerShuffleViewModel.builder()
                        .trackLink(song.getLink())
                        .build())
                .map(song -> new Gson().toJson(song))
                .orElse("{}");
        return new Gson().toJson(jsonString);
    }

    private String getShuffleAlbumPath(Resource componentResource) {
        return Optional.ofNullable(componentResource.adaptTo(MusicPlayerResourceModel.class))
                .map(MusicPlayerResourceModel::getShuffleAlbumPath)
                .orElse(StringUtils.EMPTY);
    }
}
