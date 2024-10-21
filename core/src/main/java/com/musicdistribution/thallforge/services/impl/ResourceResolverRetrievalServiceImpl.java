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

    private static final String ADMIN_SERVICE_NAME = "admin-service-user";

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    public Optional<ResourceResolver> getAdministrativeResourceResolver() {
        Map<String, Object> param = Map.of(
                ResourceResolverFactory.SUBSERVICE, ADMIN_SERVICE_NAME);
        try {
            return Optional.of(resourceResolverFactory.getServiceResourceResolver(param));
        } catch (LoginException e) {
            log.error("Could not get administrative resource resolver", e);
        }
        return Optional.empty();
    }
}
