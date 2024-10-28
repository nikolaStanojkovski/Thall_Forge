package com.musicdistribution.thallforge.services.impl;

import com.musicdistribution.thallforge.components.shared.album.AlbumMetadataViewModel;
import com.musicdistribution.thallforge.components.shared.album.AlbumMetadataViewModelProvider;
import com.musicdistribution.thallforge.components.shared.audio.AudioViewModel;
import com.musicdistribution.thallforge.services.AlbumQueryService;
import com.musicdistribution.thallforge.services.MusicPlayerShuffleService;
import com.musicdistribution.thallforge.services.impl.models.ShuffleSongViewModel;
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
                .map(albumResource -> buildShuffleSongViewModel(albumResource,
                        tracks.get(randomSongIndex), resourceResolver));
    }

    private ShuffleSongViewModel buildShuffleSongViewModel(Resource albumResource,
                                                           AudioViewModel chosenSong,
                                                           ResourceResolver resourceResolver) {
        AlbumMetadataViewModel albumMetadataViewModel = AlbumMetadataViewModelProvider.builder()
                .albumResource(albumResource)
                .resolver(resourceResolver)
                .build().getViewModel();
        return ShuffleSongViewModel.builder()
                .title(chosenSong.getTitle())
                .artist(albumMetadataViewModel.getArtist().getName())
                .coverLink(albumMetadataViewModel.getThumbnail())
                .trackLink(chosenSong.getLink())
                .build();
    }

    private int generateRandomIndex(int albumLength) {
        return (int) (Math.random() * (albumLength - 1));
    }
}
