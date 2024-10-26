package com.musicdistribution.thallforge.components.content.artistspotlight;

import com.drew.lang.annotations.Nullable;
import com.musicdistribution.thallforge.components.ViewModelProvider;
import com.musicdistribution.thallforge.components.contentfragments.artist.ArtistContentFragmentViewModel;
import com.musicdistribution.thallforge.components.contentfragments.artist.ArtistContentFragmentViewModelProvider;
import com.musicdistribution.thallforge.constants.ThallforgeConstants;
import com.musicdistribution.thallforge.services.AlbumTrackListService;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import com.musicdistribution.thallforge.utils.ImageUtils;
import com.musicdistribution.thallforge.utils.QueryUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ArtistSpotlightViewModelProvider implements ViewModelProvider<ArtistSpotlightViewModel> {

    @NonNull
    private final Resource resource;

    @NonNull
    private final ResourceResolverRetrievalService resourceResolverRetrievalService;

    @NonNull
    private final AlbumTrackListService albumTrackListService;

    @Override
    public ArtistSpotlightViewModel getViewModel() {
        return Optional.ofNullable(resource.adaptTo(ArtistSpotlightResourceModel.class))
                .flatMap(resourceModel -> resourceResolverRetrievalService.getContentDamResourceResolver()
                        .map(resourceResolver -> createViewModelWithContent(resourceModel, resourceResolver)))
                .orElseGet(this::createViewModelWithoutContent);
    }

    private ArtistSpotlightViewModel createViewModelWithContent(ArtistSpotlightResourceModel resourceModel,
                                                                ResourceResolver resourceResolver) {
        String artistReference = resourceModel.getArtistReference();
        List<ArtistSpotlightAlbumViewModel> albums = getAlbums(artistReference, resourceResolver);
        ArtistContentFragmentViewModel artistContentFragmentViewModel = getArtistData(artistReference, resourceResolver);
        return ArtistSpotlightViewModel.builder()
                .artistName(artistContentFragmentViewModel.getName())
                .artistImage(artistContentFragmentViewModel.getImage())
                .artistBiography(artistContentFragmentViewModel.getBiography())
                .albums(albums)
                .hasContent(hasContent(albums, artistContentFragmentViewModel))
                .build();
    }

    private boolean hasContent(List<ArtistSpotlightAlbumViewModel> albums,
                               ArtistContentFragmentViewModel artistContentFragmentViewModel) {
        return !albums.isEmpty() && artistContentFragmentViewModel.isHasContent();
    }

    private ArtistContentFragmentViewModel getArtistData(String artistReference, ResourceResolver resourceResolver) {
        return Optional.ofNullable(resourceResolver.getResource(artistReference))
                .map(artistContentFragmentResource -> ArtistContentFragmentViewModelProvider.builder()
                        .artistContentFragmentResource(artistContentFragmentResource)
                        .resolver(resourceResolver)
                        .build().getViewModel())
                .orElse(ArtistContentFragmentViewModel.builder().hasContent(false).build());
    }

    private List<ArtistSpotlightAlbumViewModel> getAlbums(String artistReference,
                                                          ResourceResolver resourceResolver) {
        List<ArtistSpotlightAlbumViewModel> resultList = new ArrayList<>();
        try {
            String albumSearchQuery = getAlbumSearchQuery(artistReference);
            NodeIterator nodeIterator
                    = QueryUtils.executeQuery(resourceResolver, albumSearchQuery, -1);
            if (nodeIterator != null) {
                while (nodeIterator.hasNext()) {
                    Node node = nodeIterator.nextNode();
                    Resource resultResource = resourceResolver.getResource(node.getPath());
                    if (resultResource != null) {
                        resultList.add(getAlbumViewModel(resultResource, resourceResolver));
                    }
                }
            }

        } catch (RepositoryException e) {
            log.error("Could not query album tracks for artist with reference {}", artistReference, e);
        }
        return resultList;
    }

    private ArtistSpotlightAlbumViewModel getAlbumViewModel(Resource resource, ResourceResolver resourceResolver) {
        Resource resultContentResource = resource.getChild("jcr:content");
        List<ArtistSpotlightSongViewModel> songs = getAlbumSongs(resource);
        return ArtistSpotlightAlbumViewModel.builder()
                .title(getAlbumTitle(resultContentResource))
                .thumbnail(getAlbumThumbnail(resultContentResource, resourceResolver))
                .songs(songs)
                .hasContent(!songs.isEmpty())
                .build();
    }

    private List<ArtistSpotlightSongViewModel> getAlbumSongs(Resource resource) {
        return albumTrackListService.getTracks(resource.getPath())
                .stream()
                .map(track -> ArtistSpotlightSongViewModel.builder()
                        .title(track.getTitle())
                        .link(track.getLink())
                        .build())
                .collect(Collectors.toList());
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

    private String getAlbumThumbnail(@Nullable Resource albumContentResource, ResourceResolver resourceResolver) {
        return Optional.ofNullable(albumContentResource)
                .map(this::getAlbumThumbnailPath)
                .map(resourceResolver::getResource)
                .filter(ImageUtils::isImageResource)
                .map(Resource::getPath)
                .orElse(StringUtils.EMPTY);
    }

    private String getAlbumThumbnailPath(Resource albumContentResource) {
        return String.format("%s/manualThumbnail.%s",
                albumContentResource.getPath(), ThallforgeConstants.Extensions.JPG);
    }

    private String getAlbumSearchQuery(String artistReference) {
        return "SELECT * FROM [nt:folder] AS albumNode WHERE ISDESCENDANTNODE(albumNode, '/content/dam') " +
                String.format("AND albumNode.[jcr:content/metadata/artist] = '%s' ", artistReference);
    }

    private ArtistSpotlightViewModel createViewModelWithoutContent() {
        return ArtistSpotlightViewModel.builder().hasContent(false).build();
    }
}
