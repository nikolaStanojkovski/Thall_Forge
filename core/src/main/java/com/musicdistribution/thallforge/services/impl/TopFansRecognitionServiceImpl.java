package com.musicdistribution.thallforge.services.impl;

import com.musicdistribution.thallforge.constants.ThallforgeConstants;
import com.musicdistribution.thallforge.services.TopFansRecognitionService;
import com.musicdistribution.thallforge.services.impl.models.TopFansRecognitionResourceModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;

import java.util.Optional;

@Slf4j
@Component(service = TopFansRecognitionService.class)
public class TopFansRecognitionServiceImpl implements TopFansRecognitionService {

    @Override
    public Long getMaxInteractionCount(ResourceResolver resourceResolver) {
        return Optional.ofNullable(resourceResolver.getResource(ThallforgeConstants.Paths.VAR_THALLFORGE_TOP_FANS))
                .map(topFansResource -> topFansResource.adaptTo(TopFansRecognitionResourceModel.class))
                .map(TopFansRecognitionResourceModel::getTopSiteInteractionsNumber)
                .orElse(0L);
    }

    @Override
    public void setMaxInteractionCount(ResourceResolver resourceResolver, Long maxInteractionCount) {
        Optional.ofNullable(resourceResolver.getResource(ThallforgeConstants.Paths.VAR_THALLFORGE_TOP_FANS))
                .map(topFansResource -> topFansResource.adaptTo(ModifiableValueMap.class))
                .ifPresent(topFansValueMap -> saveMaxInteractionCount(resourceResolver, topFansValueMap, maxInteractionCount));
    }

    private void saveMaxInteractionCount(ResourceResolver resourceResolver,
                                         ModifiableValueMap topFansValueMap,
                                         Long maxInteractionCount) {
        try {
            topFansValueMap.put("topSiteInteractionsNumber", maxInteractionCount);
            resourceResolver.commit();
        } catch (PersistenceException e) {
            log.error("Could not save top-site interaction count {}", maxInteractionCount, e);
        }
    }
}
