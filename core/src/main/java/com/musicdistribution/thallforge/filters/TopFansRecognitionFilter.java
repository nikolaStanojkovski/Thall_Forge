package com.musicdistribution.thallforge.filters;

import com.day.cq.wcm.api.Page;
import com.musicdistribution.thallforge.constants.ThallforgeConstants;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import com.musicdistribution.thallforge.services.SystemConfigurationService;
import com.musicdistribution.thallforge.services.TopFansRecognitionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.engine.EngineConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component(
        service = Filter.class,
        property = {
                EngineConstants.SLING_FILTER_SCOPE + "=REQUEST",
                EngineConstants.SLING_FILTER_PATTERN + "=^/content/.*\\.html$"
        }
)
public class TopFansRecognitionFilter implements Filter {

    @Reference
    private ResourceResolverRetrievalService resourceResolverRetrievalService;

    @Reference
    private TopFansRecognitionService topFansRecognitionService;

    @Reference
    private SystemConfigurationService systemConfigurationService;

    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain filterChain) throws IOException, ServletException {
        final SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;
        if (systemConfigurationService.isPublish() && isCurrentRequestFromPage(slingRequest)) {
            HttpSession userSession = getUserSession(slingRequest);
            resetBannerDisplay(userSession);
            final Long interactionCount = (Long) userSession.getAttribute(ThallforgeConstants.Session.INTERACTION_COUNT_ATTR);
            if (interactionCount != null) {
                updateInteractionCount(userSession, interactionCount);
                userSession.setAttribute(ThallforgeConstants.Session.INTERACTION_COUNT_ATTR, interactionCount + 1);
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isCurrentRequestFromPage(final SlingHttpServletRequest request) {
        Resource resource = request.getResource();
        return Optional.ofNullable(resource.adaptTo(Page.class))
                .map(Page::hasContent)
                .orElse(false);
    }

    private void updateInteractionCount(final HttpSession userSession, final Long interactionCount) {
        resourceResolverRetrievalService.getAdministrativeResourceResolver()
                .ifPresent(resourceResolver -> {
                    Long maxInteractionCount = topFansRecognitionService.getMaxInteractionCount(resourceResolver);
                    if (interactionCount > maxInteractionCount) {
                        topFansRecognitionService.setMaxInteractionCount(resourceResolver, interactionCount);
                        if (shouldDisplayBanner(userSession)) {
                            userSession.setAttribute(ThallforgeConstants.Session.BANNER_DISPLAY_ATTR, true);
                        }
                    }
                });
    }

    private boolean shouldDisplayBanner(final HttpSession userSession) {
        return !isBannerDisplayLocked(userSession) && !getBannerDisplay(userSession);
    }

    private void resetBannerDisplay(final HttpSession userSession) {
        boolean bannerDisplay = getBannerDisplay(userSession);
        if (bannerDisplay) {
            userSession.setAttribute(ThallforgeConstants.Session.BANNER_DISPLAY_LOCKED_ATTR, true);
        }
    }

    private boolean isBannerDisplayLocked(final HttpSession userSession) {
        return Optional.ofNullable(userSession.getAttribute(ThallforgeConstants.Session.BANNER_DISPLAY_LOCKED_ATTR))
                .map(bannerDisplay -> (boolean) bannerDisplay)
                .orElse(false);
    }

    private boolean getBannerDisplay(final HttpSession userSession) {
        return Optional.ofNullable(userSession.getAttribute(ThallforgeConstants.Session.BANNER_DISPLAY_ATTR))
                .map(bannerDisplay -> (boolean) bannerDisplay)
                .orElse(false);
    }

    private HttpSession getUserSession(final SlingHttpServletRequest request) {
        return request.getSession(true);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(final FilterConfig filterConfig) {
    }
}
