package com.musicdistribution.thallforge.components.structure.latestreleases;

import com.musicdistribution.thallforge.components.ViewModelProvider;
import com.musicdistribution.thallforge.constants.ThallforgeConstants;
import com.musicdistribution.thallforge.services.AlbumQueryService;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import com.musicdistribution.thallforge.services.impl.models.AlbumViewModel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

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
                .flatMap(resourceModel -> resourceResolverRetrievalService.getContentDamResourceResolver()
                        .map(resourceResolver -> createViewModel(resourceResolver, resourceModel)))
                .orElseGet(this::createViewModelWithoutContent);
    }

    private LatestReleasesViewModel createViewModel(ResourceResolver resourceResolver,
                                                    LatestReleasesResourceModel resourceModel) {
        List<AlbumViewModel> albums = albumQueryService
                .getAlbums(resourceResolver, getAlbumSearchQuery(), resourceModel.getLimit());
        return LatestReleasesViewModel.builder()
                .title(resourceModel.getTitle())
                .albums(albums)
                .hasContent(!albums.isEmpty())
                .build();
    }

    private String getAlbumSearchQuery() {
        return "SELECT * FROM [nt:folder] AS albumNode WHERE ISDESCENDANTNODE(albumNode, '/content/dam') " +
                String.format("AND albumNode.[jcr:content/folderMetadataSchema] = '%s'",
                        ThallforgeConstants.MetadataSchema.ALBUM_METADATA_SCHEMA) +
                "ORDER BY albumNode.[jcr:content/metadata/date] DESC";
    }

    private LatestReleasesViewModel createViewModelWithoutContent() {
        return LatestReleasesViewModel.builder().hasContent(false).build();
    }
}
