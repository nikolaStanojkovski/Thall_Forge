package com.musicdistribution.thallforge.utils;

import com.day.cq.dam.commons.util.DamUtil;
import org.apache.sling.api.resource.Resource;

import java.util.Optional;

public final class ContentFragmentUtils {

    public static boolean isContentFragment(Resource contentFragmentResource) {
        if (!DamUtil.isAsset(contentFragmentResource)) {
            return false;
        }
        return Optional.ofNullable(contentFragmentResource.getChild("jcr:content"))
                .map(Resource::getValueMap)
                .map(r -> r.get("contentFragment", false))
                .orElse(false);
    }
}
