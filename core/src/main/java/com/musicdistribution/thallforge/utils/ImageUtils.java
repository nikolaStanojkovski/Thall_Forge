package com.musicdistribution.thallforge.utils;

import com.musicdistribution.thallforge.constants.ThallforgeConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;

import java.util.Optional;

@Slf4j
public final class ImageUtils {

    public static boolean isImageResource(Resource imageResource) {
        return Optional.ofNullable(imageResource.getChild("jcr:content"))
                .map(Resource::getValueMap)
                .map(valueMap -> valueMap.getOrDefault("jcr:mimeType", StringUtils.EMPTY))
                .map(ThallforgeConstants.MimeTypes.ALL_IMAGE_MIME_TYPES::contains)
                .orElse(false);
    }
}
