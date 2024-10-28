package com.musicdistribution.thallforge.services;

import org.apache.sling.api.resource.ResourceResolver;

import java.util.Optional;

public interface ResourceResolverRetrievalService {

    Optional<ResourceResolver> getAdministrativeResourceResolver();

    Optional<ResourceResolver> getContentDamResourceResolver();

    Optional<ResourceResolver> getContentReaderResourceResolver();
}
