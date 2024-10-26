package com.musicdistribution.thallforge.components.content.albumtracklist;

import com.musicdistribution.thallforge.components.ViewModelProvider;
import com.musicdistribution.thallforge.components.shared.album.AlbumMetadataViewModel;
import com.musicdistribution.thallforge.components.shared.album.AlbumMetadataViewModelProvider;
import com.musicdistribution.thallforge.components.shared.audio.AudioViewModel;
import com.musicdistribution.thallforge.services.AlbumQueryService;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.List;
import java.util.Optional;

@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AlbumTrackListViewModelProvider implements ViewModelProvider<AlbumTrackListViewModel> {

    @NonNull
    private final Resource resource;

    @NonNull
    private final ResourceResolverRetrievalService resourceResolverRetrievalService;

    @NonNull
    private final AlbumQueryService albumQueryService;

    @Override
    public AlbumTrackListViewModel getViewModel() {
        return Optional.ofNullable(resource.adaptTo(AlbumTrackListResourceModel.class))
                .filter(this::hasContent)
                .map(this::createViewModel)
                .orElseGet(this::createViewModelWithoutContent);
    }

    private boolean hasContent(AlbumTrackListResourceModel resourceModel) {
        return resourceModel != null && StringUtils.isNotBlank(resourceModel.getAlbumPath());
    }

    private AlbumTrackListViewModel createViewModel(AlbumTrackListResourceModel resourceModel) {
        String albumPath = resourceModel.getAlbumPath();
        return resourceResolverRetrievalService.getContentDamResourceResolver()
                .flatMap(resourceResolver -> Optional.ofNullable(resourceResolver.getResource(albumPath))
                        .map(albumResource -> createViewModelWithContent(resourceModel, albumResource, albumPath, resourceResolver)))
                .orElseGet(this::createViewModelWithoutContent);
    }

    private AlbumTrackListViewModel createViewModelWithContent(AlbumTrackListResourceModel resourceModel,
                                                               Resource albumResource, String albumPath,
                                                               ResourceResolver resourceResolver) {
        List<AudioViewModel> tracks = albumQueryService.getAlbumTracks(resourceResolver, albumPath);
        AlbumMetadataViewModel albumMetadataViewModel = AlbumMetadataViewModelProvider.builder()
                .albumResource(albumResource)
                .resolver(resourceResolver)
                .build().getViewModel();
        return AlbumTrackListViewModel.builder()
                .title(albumMetadataViewModel.getTitle())
                .thumbnail(albumMetadataViewModel.getThumbnail())
                .downloadLabel(resourceModel.getDownloadLabel())
                .tracks(tracks)
                .hasContent(!tracks.isEmpty())
                .build();
    }

    private AlbumTrackListViewModel createViewModelWithoutContent() {
        return AlbumTrackListViewModel.builder().hasContent(false).build();
    }
}
