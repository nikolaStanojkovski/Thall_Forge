package com.musicdistribution.thallforge.services.impl;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.commons.util.DamUtil;
import com.drew.lang.annotations.Nullable;
import com.musicdistribution.thallforge.components.contentfragments.artist.ArtistContentFragmentResourceModel;
import com.musicdistribution.thallforge.components.shared.audio.AudioViewModel;
import com.musicdistribution.thallforge.components.shared.audio.AudioViewModelProvider;
import com.musicdistribution.thallforge.constants.ThallforgeConstants;
import com.musicdistribution.thallforge.services.AlbumQueryService;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import com.musicdistribution.thallforge.services.impl.models.AlbumViewModel;
import com.musicdistribution.thallforge.utils.AudioUtils;
import com.musicdistribution.thallforge.utils.ImageUtils;
import com.musicdistribution.thallforge.utils.QueryUtils;
import com.musicdistribution.thallforge.utils.ResourceUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
@Component(service = AlbumQueryService.class)
public class AlbumQueryServiceImpl implements AlbumQueryService {

    @Reference
    private ResourceResolverRetrievalService resourceResolverRetrievalService;

    @Override
    public List<AudioViewModel> getAlbumTracks(ResourceResolver resourceResolver, String albumPath) {
        if (StringUtils.isBlank(albumPath)) {
            return Collections.emptyList();
        }
        return Optional.ofNullable(resourceResolver.getResource(albumPath))
                .map(this::getTracklistStream)
                .orElse(Stream.empty())
                .filter(this::isAudioAsset)
                .map(this::getSongViewModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<AlbumViewModel> getAlbums(ResourceResolver resourceResolver, String searchQuery, int limit) {
        try {
            List<AlbumViewModel> resultList = new ArrayList<>();
            String query = StringUtils.isNotBlank(searchQuery) ? searchQuery : getDefaultSearchQuery();
            NodeIterator nodeIterator = QueryUtils.executeQuery(resourceResolver, query, limit);
            if (nodeIterator == null) {
                return Collections.emptyList();
            }
            while (nodeIterator.hasNext()) {
                Node node = nodeIterator.nextNode();
                Resource resultResource = resourceResolver.getResource(node.getPath());
                if (resultResource != null) {
                    resultList.add(getAlbumViewModel(resultResource, resourceResolver));
                }
            }
            return resultList;
        } catch (RepositoryException e) {
            log.error("Could not search the albums with search query {} and limit {}", searchQuery, limit, e);
        }
        return Collections.emptyList();
    }

    private String getDefaultSearchQuery() {
        return "SELECT * FROM [nt:folder] AS albumNode WHERE ISDESCENDANTNODE(albumNode, '/content/dam')";
    }

    private AlbumViewModel getAlbumViewModel(Resource resultResource, ResourceResolver resourceResolver) {
        Resource resultContentResource = resultResource.getChild("jcr:content");
        return AlbumViewModel.builder()
                .id(ResourceUtils.generateId(resultResource))
                .link(resultResource.getPath())
                .title(getAlbumTitle(resultContentResource))
                .artist(getAlbumArtist(resultContentResource, resourceResolver))
                .thumbnail(getAlbumThumbnail(resultContentResource, resourceResolver))
                .songs(getAlbumTracks(resourceResolver, resultResource.getPath()))
                .build();
    }

    private String getAlbumArtist(@Nullable Resource albumContentResource,
                                  ResourceResolver resourceResolver) {
        return Optional.ofNullable(albumContentResource)
                .map(a -> a.getChild("metadata"))
                .map(Resource::getValueMap)
                .map(s -> s.get("artist", StringUtils.EMPTY))
                .map(resourceResolver::getResource)
                .map(artistResource -> artistResource.getChild("jcr:content/data/master"))
                .map(masterContentFragmentResource -> masterContentFragmentResource.adaptTo(ArtistContentFragmentResourceModel.class))
                .map(ArtistContentFragmentResourceModel::getName)
                .orElse(StringUtils.EMPTY);
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

    private boolean isAudioAsset(Resource songResource) {
        if (!DamUtil.isAsset(songResource)) {
            return false;
        }
        return Optional.ofNullable(songResource.adaptTo(Asset.class))
                .map(AudioUtils::isAudioAsset)
                .orElse(false);
    }


    private Stream<Resource> getTracklistStream(Resource albumResource) {
        return StreamSupport.stream(albumResource.getChildren().spliterator(), false);
    }

    private AudioViewModel getSongViewModel(Resource songResource) {
        return AudioViewModelProvider.builder()
                .audioResource(songResource)
                .build()
                .getViewModel();
    }
}
