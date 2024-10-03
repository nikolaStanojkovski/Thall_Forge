package com.musicdistribution.thallcore.services;

import org.apache.sling.api.resource.ResourceResolver;

import java.util.Optional;

public interface ResourceResolverRetrievalService {

    Optional<ResourceResolver> getAdministrativeResourceResolver();
}
