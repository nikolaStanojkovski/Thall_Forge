package com.musicdistribution.thallforge.components.content.albumtracklist;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.commons.util.DamUtil;
import com.musicdistribution.thallforge.components.ViewModelProvider;
import com.musicdistribution.thallforge.components.shared.audio.AudioViewModel;
import com.musicdistribution.thallforge.services.AlbumTrackListService;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

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
    private final AlbumTrackListService albumTrackListService;

    @Override
    public AlbumTrackListViewModel getViewModel() {
        return Optional.ofNullable(resource.adaptTo(AlbumTrackListResourceModel.class))
                .map(this::createViewModelWithContent)
                .orElseGet(this::createViewModelWithoutContent);
    }

    private AlbumTrackListViewModel createViewModelWithContent(AlbumTrackListResourceModel resourceModel) {
        List<AudioViewModel> tracks = albumTrackListService.getTracks(resourceModel.getAlbumPath());
        return AlbumTrackListViewModel.builder()
                .title(getAlbumTitle())
                .thumbnail(getAlbumThumbnail())
                .downloadLabel(resourceModel.getDownloadLabel())
                .tracks(tracks)
                .hasContent(!tracks.isEmpty())
                .build();
    }

    private String getAlbumTitle() {
        String title = Optional.ofNullable(resource.adaptTo(ValueMap.class))
                .map(properties -> properties.get("jcr:title", String.class))
                .orElse(resource.getName());
        return StringUtils.defaultString(title);
    }

    private String getAlbumThumbnail() {
        return Optional.ofNullable(resource.adaptTo(ValueMap.class))
                .map(properties -> properties.get("jcr:thumbnail", String.class))
                .filter(this::isValidAlbumThumbnail)
                .orElse(StringUtils.EMPTY);
    }

    private boolean isValidAlbumThumbnail(String albumThumbnail) {
        return resourceResolverRetrievalService.getAdministrativeResourceResolver()
                .map(resourceResolver -> resourceResolver.getResource(albumThumbnail))
                .map(r -> r.adaptTo(Asset.class))
                .map(DamUtil::isImage)
                .orElse(false);
    }

    private AlbumTrackListViewModel createViewModelWithoutContent() {
        return AlbumTrackListViewModel.builder().hasContent(false).build();
    }
}
