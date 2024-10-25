package com.musicdistribution.thallforge.utils;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.commons.util.DamUtil;
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

    public static boolean isImageAsset(Resource imageResource) {
        if (!DamUtil.isAsset(imageResource)) {
            return false;
        }
        return Optional.ofNullable(imageResource.adaptTo(Asset.class))
                .map(DamUtil::isImage)
                .orElse(false);
    }
}
