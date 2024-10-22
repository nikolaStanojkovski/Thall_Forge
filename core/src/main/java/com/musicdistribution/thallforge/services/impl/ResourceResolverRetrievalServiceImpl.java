package com.musicdistribution.thallforge.services.impl;

import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component(service = ResourceResolverRetrievalService.class)
public class ResourceResolverRetrievalServiceImpl implements ResourceResolverRetrievalService {

    private static final String ADMIN_SERVICE_NAME = "user-admin-service";
    private static final String DAM_SERVICE_NAME = "user-dam-service";

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    public Optional<ResourceResolver> getAdministrativeResourceResolver() {
        return getResourceResolver(Map.of(
                ResourceResolverFactory.SUBSERVICE, ADMIN_SERVICE_NAME));
    }

    @Override
    public Optional<ResourceResolver> getContentDamResourceResolver() {
        return getResourceResolver(Map.of(
                ResourceResolverFactory.SUBSERVICE, DAM_SERVICE_NAME));
    }

    private Optional<ResourceResolver> getResourceResolver(Map<String, Object> serviceParams) {
        try {
            return Optional.of(resourceResolverFactory.getServiceResourceResolver(serviceParams));
        } catch (LoginException e) {
            log.error("Could not get administrative resource resolver", e);
        }
        return Optional.empty();
    }
}
