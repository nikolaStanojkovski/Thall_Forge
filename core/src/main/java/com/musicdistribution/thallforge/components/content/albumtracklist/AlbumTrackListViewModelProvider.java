package com.musicdistribution.thallforge.components.content.albumtracklist;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.commons.util.DamUtil;
import com.musicdistribution.thallforge.components.ViewModelProvider;
import com.musicdistribution.thallforge.components.shared.audio.AudioViewModel;
import com.musicdistribution.thallforge.constants.ThallforgeConstants;
import com.musicdistribution.thallforge.services.AlbumTrackListService;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
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
                .filter(this::hasContent)
                .map(this::createViewModel)
                .orElseGet(this::createViewModelWithoutContent);
    }

    private boolean hasContent(AlbumTrackListResourceModel resourceModel) {
        return resourceModel != null && StringUtils.isNotBlank(resourceModel.getAlbumPath());
    }

    private AlbumTrackListViewModel createViewModel(AlbumTrackListResourceModel resourceModel) {
        String albumPath = resourceModel.getAlbumPath();
        return resourceResolverRetrievalService.getAdministrativeResourceResolver()
                .flatMap(resourceResolver -> Optional.ofNullable(resourceResolver.getResource(albumPath))
                        .map(r -> r.getChild("jcr:content"))
                        .map(albumResource -> createViewModelWithContent(resourceModel, albumResource, albumPath, resourceResolver)))
                .orElseGet(this::createViewModelWithoutContent);
    }

    private AlbumTrackListViewModel createViewModelWithContent(AlbumTrackListResourceModel resourceModel,
                                                               Resource albumContentResource, String albumPath,
                                                               ResourceResolver resourceResolver) {
        List<AudioViewModel> tracks = albumTrackListService.getTracks(albumPath);
        return AlbumTrackListViewModel.builder()
                .title(getAlbumTitle(albumContentResource))
                .thumbnail(getAlbumThumbnail(albumContentResource, resourceResolver))
                .downloadLabel(resourceModel.getDownloadLabel())
                .tracks(tracks)
                .hasContent(!tracks.isEmpty())
                .build();
    }

    private String getAlbumTitle(Resource albumContentResource) {
        String title = Optional.ofNullable(albumContentResource.adaptTo(ValueMap.class))
                .map(properties -> properties.get("jcr:title", String.class))
                .orElse(albumContentResource.getName());
        return StringUtils.defaultString(title);
    }

    private String getAlbumThumbnail(Resource albumContentResource, ResourceResolver resourceResolver) {
        return Optional.ofNullable(resourceResolver.getResource(getAlbumThumbnailPath(albumContentResource)))
                .map(Resource::getPath)
                .filter(thumbnailPath -> isValidAlbumThumbnail(thumbnailPath, resourceResolver))
                .orElse(StringUtils.EMPTY);
    }

    private String getAlbumThumbnailPath(Resource albumContentResource) {
        return String.format("%s/manualThumbnail.%s",
                albumContentResource.getPath(), ThallforgeConstants.Extensions.JPG);
    }

    private boolean isValidAlbumThumbnail(String albumThumbnail, ResourceResolver resourceResolver) {
        return Optional.ofNullable(resourceResolver.getResource(albumThumbnail))
                .map(r -> r.adaptTo(Asset.class))
                .map(DamUtil::isImage)
                .orElse(false);
    }

    private AlbumTrackListViewModel createViewModelWithoutContent() {
        return AlbumTrackListViewModel.builder().hasContent(false).build();
    }
}
