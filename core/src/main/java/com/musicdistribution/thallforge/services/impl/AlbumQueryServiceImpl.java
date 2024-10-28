package com.musicdistribution.thallforge.services.impl;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.commons.util.DamUtil;
import com.musicdistribution.thallforge.components.shared.album.AlbumMetadataViewModel;
import com.musicdistribution.thallforge.components.shared.album.AlbumMetadataViewModelProvider;
import com.musicdistribution.thallforge.components.shared.audio.AudioViewModel;
import com.musicdistribution.thallforge.components.shared.audio.AudioViewModelProvider;
import com.musicdistribution.thallforge.constants.ThallforgeConstants;
import com.musicdistribution.thallforge.services.AlbumQueryService;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import com.musicdistribution.thallforge.services.impl.models.AlbumViewModel;
import com.musicdistribution.thallforge.utils.AudioUtils;
import com.musicdistribution.thallforge.utils.QueryUtils;
import com.musicdistribution.thallforge.utils.ResourceUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
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
        return "SELECT * FROM [nt:folder] AS albumNode WHERE ISDESCENDANTNODE(albumNode, '/content/dam')"
                + String.format("AND albumNode.[jcr:content/folderMetadataSchema] = '%s'",
                ThallforgeConstants.MetadataSchema.ALBUM_METADATA_SCHEMA);
    }

    private AlbumViewModel getAlbumViewModel(Resource resultResource, ResourceResolver resourceResolver) {
        AlbumMetadataViewModel albumMetadataViewModel = AlbumMetadataViewModelProvider.builder()
                .albumResource(resultResource)
                .resolver(resourceResolver)
                .build().getViewModel();
        return AlbumViewModel.builder()
                .id(ResourceUtils.generateId(resultResource))
                .link(resultResource.getPath())
                .title(albumMetadataViewModel.getTitle())
                .artist(albumMetadataViewModel.getArtist().getName())
                .thumbnail(albumMetadataViewModel.getThumbnail())
                .releaseDate(albumMetadataViewModel.getDate())
                .songs(getAlbumTracks(resourceResolver, resultResource.getPath()))
                .build();
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
