package com.musicdistribution.thallforge.components.shared.album;

import com.drew.lang.annotations.Nullable;
import com.musicdistribution.thallforge.components.ViewModelProvider;
import com.musicdistribution.thallforge.components.shared.artist.ArtistContentFragmentViewModel;
import com.musicdistribution.thallforge.components.shared.artist.ArtistContentFragmentViewModelProvider;
import com.musicdistribution.thallforge.constants.ThallforgeConstants;
import com.musicdistribution.thallforge.utils.ImageUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;

import java.util.Optional;

@Slf4j
@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AlbumMetadataViewModelProvider implements ViewModelProvider<AlbumMetadataViewModel> {

    @NonNull
    private final ResourceResolver resolver;

    @NonNull
    private final Resource albumResource;

    @Override
    public AlbumMetadataViewModel getViewModel() {
        return Optional.ofNullable(albumResource.getChild("jcr:content"))
                .flatMap(albumContentResource -> Optional.ofNullable(albumContentResource.getChild("metadata"))
                        .map(metadataResource -> metadataResource.adaptTo(AlbumMetadataResourceModel.class))
                        .map(metadataResourceModel -> createViewModelWithContent(metadataResourceModel, albumContentResource)))
                .orElseGet(this::createViewModelWithoutContent);
    }

    private AlbumMetadataViewModel createViewModelWithContent(AlbumMetadataResourceModel resourceModel,
                                                              Resource albumContentResource) {
        return AlbumMetadataViewModel.builder()
                .title(getAlbumTitle(albumContentResource))
                .genre(resourceModel.getGenre())
                .artist(getAlbumArtist(resourceModel))
                .date(getAlbumReleaseDate(resourceModel))
                .thumbnail(getAlbumThumbnail(albumContentResource, resolver))
                .build();
    }

    private String getAlbumThumbnailPath(Resource albumContentResource) {
        return String.format("%s/manualThumbnail.%s",
                albumContentResource.getPath(), ThallforgeConstants.Extensions.JPG);
    }

    private String getAlbumThumbnail(@Nullable Resource albumContentResource, ResourceResolver resourceResolver) {
        return Optional.ofNullable(albumContentResource)
                .map(this::getAlbumThumbnailPath)
                .map(resourceResolver::getResource)
                .filter(ImageUtils::isImageResource)
                .map(Resource::getPath)
                .orElse(StringUtils.EMPTY);
    }

    private ArtistContentFragmentViewModel getAlbumArtist(AlbumMetadataResourceModel resourceModel) {
        return Optional.ofNullable(resourceModel.getArtist())
                .map(resolver::getResource)
                .map(artistResource -> ArtistContentFragmentViewModelProvider
                        .builder()
                        .artistContentFragmentResource(artistResource)
                        .resolver(resolver)
                        .build().getViewModel())
                .orElse(ArtistContentFragmentViewModel.builder().build());
    }

    private String getAlbumReleaseDate(AlbumMetadataResourceModel resourceModel) {
        return StringUtils.EMPTY;
    }

    private String getAlbumTitle(@Nullable Resource albumContentResource) {
        if (albumContentResource == null) {
            return StringUtils.EMPTY;
        }
        String title = Optional.ofNullable(albumContentResource.adaptTo(ValueMap.class))
                .map(properties -> properties.get("jcr:title", String.class))
                .orElse(albumContentResource.getName());
        return StringUtils.defaultString(title);
    }

    private AlbumMetadataViewModel createViewModelWithoutContent() {
        return AlbumMetadataViewModel.builder().build();
    }
}
