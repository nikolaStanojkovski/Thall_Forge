package com.musicdistribution.thallforge.utils;

import com.drew.lang.annotations.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

public final class ResourceUtils {

    public static String generateId(@Nullable Resource resource) {
        return Optional.ofNullable(resource)
                .map(Resource::getPath)
                .map(resourcePath -> resourcePath.getBytes(StandardCharsets.UTF_8))
                .map(UUID::nameUUIDFromBytes)
                .map(UUID::toString)
                .orElse(StringUtils.EMPTY);
    }
}
