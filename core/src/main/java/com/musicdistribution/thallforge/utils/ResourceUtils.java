package com.musicdistribution.thallforge.utils;

import com.drew.lang.annotations.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class ResourceUtils {

    public static String generateId(@Nullable Resource resource) {
        return Optional.ofNullable(resource)
                .map(Resource::getPath)
                .map(resourcePath -> resourcePath.getBytes(StandardCharsets.UTF_8))
                .map(UUID::nameUUIDFromBytes)
                .map(UUID::toString)
                .orElse(StringUtils.EMPTY);
    }

    public static Stream<Resource> getAllChildren(Resource parentResource) {
        Stream<Resource> currentChildren = StreamSupport.stream(parentResource.getChildren().spliterator(), false);
        return currentChildren.flatMap(child -> Stream.concat(Stream.of(child), getAllChildren(child)));
    }
}
