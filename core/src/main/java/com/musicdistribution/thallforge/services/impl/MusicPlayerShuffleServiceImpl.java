package com.musicdistribution.thallforge.services.impl;

import com.musicdistribution.thallforge.components.contentfragments.artist.ArtistContentFragmentResourceModel;
import com.musicdistribution.thallforge.components.shared.audio.AudioViewModel;
import com.musicdistribution.thallforge.constants.ThallforgeConstants;
import com.musicdistribution.thallforge.services.AlbumQueryService;
import com.musicdistribution.thallforge.services.MusicPlayerShuffleService;
import com.musicdistribution.thallforge.services.impl.models.ShuffleSongViewModel;
import com.musicdistribution.thallforge.utils.ImageUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.List;
import java.util.Optional;

@Component(service = MusicPlayerShuffleService.class)
public class MusicPlayerShuffleServiceImpl implements MusicPlayerShuffleService {

    @Reference
    private AlbumQueryService albumQueryService;

    @Override
    public Optional<ShuffleSongViewModel> shuffle(ResourceResolver resourceResolver, String albumPath) {
        List<AudioViewModel> tracks = albumQueryService.getAlbumTracks(resourceResolver, albumPath);
        return getRandomSong(resourceResolver, albumPath, tracks);
    }

    private Optional<ShuffleSongViewModel> getRandomSong(ResourceResolver resourceResolver,
                                                         String albumPath, List<AudioViewModel> tracks) {
        int randomSongIndex = generateRandomIndex(tracks.size());
        return Optional.ofNullable(resourceResolver.getResource(albumPath))
                .map(r -> r.getChild("jcr:content"))
                .map(albumResource -> buildShuffleSongViewModel(albumResource,
                        tracks.get(randomSongIndex), resourceResolver));
    }

    private ShuffleSongViewModel buildShuffleSongViewModel(Resource albumResource,
                                                           AudioViewModel chosenSong,
                                                           ResourceResolver resourceResolver) {
        return ShuffleSongViewModel.builder()
                .title(chosenSong.getTitle())
                .artist(getAlbumArtist(albumResource, resourceResolver))
                .coverLink(getAlbumThumbnail(albumResource, resourceResolver))
                .trackLink(chosenSong.getLink())
                .build();
    }

    private String getAlbumThumbnail(Resource albumContentResource, ResourceResolver resourceResolver) {
        String albumThumbnailPath = getAlbumThumbnailPath(albumContentResource);
        return Optional.ofNullable(resourceResolver.getResource(albumThumbnailPath))
                .filter(ImageUtils::isImageResource)
                .map(Resource::getPath)
                .orElse(StringUtils.EMPTY);
    }

    private String getAlbumArtist(Resource albumContentResource,
                                  ResourceResolver resourceResolver) {
        return Optional.ofNullable(albumContentResource.getChild("metadata"))
                .map(Resource::getValueMap)
                .map(valueMap -> valueMap.get("artist", StringUtils.EMPTY))
                .map(resourceResolver::getResource)
                .map(artistResource -> artistResource.getChild("jcr:content/data/master"))
                .map(masterContentFragmentResource -> masterContentFragmentResource.adaptTo(ArtistContentFragmentResourceModel.class))
                .map(ArtistContentFragmentResourceModel::getName)
                .orElse(StringUtils.EMPTY);
    }

    private String getAlbumThumbnailPath(Resource albumContentResource) {
        return String.format("%s/manualThumbnail.%s",
                albumContentResource.getPath(), ThallforgeConstants.Extensions.JPG);
    }

    private int generateRandomIndex(int albumLength) {
        return (int) (Math.random() * (albumLength - 1));
    }
}
