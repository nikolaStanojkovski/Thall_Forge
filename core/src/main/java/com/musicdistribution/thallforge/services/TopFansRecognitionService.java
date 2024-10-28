package com.musicdistribution.thallforge.services;

import org.apache.sling.api.resource.ResourceResolver;

public interface TopFansRecognitionService {

    Long getMaxInteractionCount(ResourceResolver resourceResolver);

    void setMaxInteractionCount(ResourceResolver resourceResolver, Long maxInteractionCount);
}
