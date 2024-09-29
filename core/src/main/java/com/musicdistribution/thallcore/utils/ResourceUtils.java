package com.musicdistribution.thallcore.utils;

import com.drew.lang.annotations.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;

import java.util.Optional;
import java.util.UUID;

public final class ResourceUtils {

    public static String generateId(@Nullable Resource resource) {
        return Optional.ofNullable(resource)
                .map(Resource::getPath)
                .map(UUID::fromString)
                .map(UUID::toString)
                .orElse(StringUtils.EMPTY);
    }
}
