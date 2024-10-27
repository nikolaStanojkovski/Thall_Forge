package com.musicdistribution.thallforge.components.structure.topfansbanner;

import com.musicdistribution.thallforge.components.ViewModelProvider;
import com.musicdistribution.thallforge.constants.ThallforgeConstants;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.sling.api.SlingHttpServletRequest;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TopFansBannerViewModelProvider implements ViewModelProvider<TopFansBannerViewModel> {

    @NonNull
    private final SlingHttpServletRequest request;

    @Override
    public TopFansBannerViewModel getViewModel() {
        boolean displayBanner = Optional.ofNullable(getUserSession(request))
                .map(this::showBannerDisplay).orElse(false);
        return TopFansBannerViewModel.builder()
                .displayBanner(displayBanner)
                .build();
    }

    private boolean showBannerDisplay(final HttpSession userSession) {
        boolean bannerDisplay = Optional.ofNullable(userSession.getAttribute(ThallforgeConstants.Session.BANNER_DISPLAY_ATTR))
                .map(banner -> (boolean) banner)
                .orElse(false);
        boolean bannerDisplayLocked = Optional.ofNullable(userSession.getAttribute(ThallforgeConstants.Session.BANNER_DISPLAY_LOCKED_ATTR))
                .map(banner -> (boolean) banner)
                .orElse(false);
        return bannerDisplay && !bannerDisplayLocked;
    }

    private HttpSession getUserSession(final SlingHttpServletRequest request) {
        return request.getSession(true);
    }
}
