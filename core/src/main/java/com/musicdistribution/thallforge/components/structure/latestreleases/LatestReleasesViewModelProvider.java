package com.musicdistribution.thallforge.components.structure.latestreleases;

import com.musicdistribution.thallforge.components.ViewModelProvider;
import com.musicdistribution.thallforge.components.shared.audio.AudioViewModel;
import com.musicdistribution.thallforge.constants.ThallforgeConstants;
import com.musicdistribution.thallforge.services.AlbumQueryService;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import com.musicdistribution.thallforge.utils.ImageUtils;
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
public class LatestReleasesViewModelProvider implements ViewModelProvider<LatestReleasesViewModel> {

    @NonNull
    private final Resource resource;

    @NonNull
    private final ResourceResolverRetrievalService resourceResolverRetrievalService;

    @NonNull
    private final AlbumQueryService albumQueryService;

    @Override
    public LatestReleasesViewModel getViewModel() {
        return Optional.ofNullable(resource.adaptTo(LatestReleasesResourceModel.class))
                .filter(this::hasContent)
                .map(this::createViewModel)
                .orElseGet(this::createViewModelWithoutContent);
    }

    private boolean hasContent(LatestReleasesResourceModel resourceModel) {
        return resourceModel != null && StringUtils.isNotBlank(resourceModel.getAlbumPath());
    }

    private LatestReleasesViewModel createViewModel(LatestReleasesResourceModel resourceModel) {
        String albumPath = resourceModel.getAlbumPath();
        return resourceResolverRetrievalService.getContentDamResourceResolver()
                .flatMap(resourceResolver -> Optional.ofNullable(resourceResolver.getResource(albumPath))
                        .map(r -> r.getChild("jcr:content"))
                        .map(albumResource -> createViewModelWithContent(resourceModel, albumResource, albumPath, resourceResolver)))
                .orElseGet(this::createViewModelWithoutContent);
    }

    private LatestReleasesViewModel createViewModelWithContent(LatestReleasesResourceModel resourceModel,
                                                               Resource albumContentResource, String albumPath,
                                                               ResourceResolver resourceResolver) {
        List<AudioViewModel> tracks = albumQueryService.getAlbumTracks(albumPath);
        return LatestReleasesViewModel.builder()
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
        String albumThumbnailPath = getAlbumThumbnailPath(albumContentResource);
        return Optional.ofNullable(resourceResolver.getResource(albumThumbnailPath))
                .filter(ImageUtils::isImageResource)
                .map(Resource::getPath)
                .orElse(StringUtils.EMPTY);
    }

    private String getAlbumThumbnailPath(Resource albumContentResource) {
        return String.format("%s/manualThumbnail.%s",
                albumContentResource.getPath(), ThallforgeConstants.Extensions.JPG);
    }

    private LatestReleasesViewModel createViewModelWithoutContent() {
        return LatestReleasesViewModel.builder().hasContent(false).build();
    }
}
