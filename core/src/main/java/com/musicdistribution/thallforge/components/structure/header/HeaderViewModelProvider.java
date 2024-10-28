package com.musicdistribution.thallforge.components.structure.header;

import com.day.cq.wcm.api.Page;
import com.musicdistribution.thallforge.components.ViewModelProvider;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import com.musicdistribution.thallforge.utils.NavUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class HeaderViewModelProvider implements ViewModelProvider<HeaderViewModel> {

    @NonNull
    private final Resource resource;

    @NonNull
    private final ResourceResolverRetrievalService resourceResolverRetrievalService;

    @Override
    public HeaderViewModel getViewModel() {
        return Optional.ofNullable(resource.adaptTo(HeaderResourceModel.class))
                .flatMap(resourceModel -> resourceResolverRetrievalService.getContentReaderResourceResolver()
                        .map(resourceResolver -> createViewModel(resourceResolver, resourceModel)))
                .orElseGet(this::createViewModelWithoutContent);
    }

    private HeaderViewModel createViewModel(ResourceResolver resourceResolver,
                                            HeaderResourceModel resourceModel) {
        List<HeaderItemViewModel> items = getItems(resourceResolver, resourceModel);
        return HeaderViewModel.builder()
                .items(items)
                .hasContent(!items.isEmpty())
                .build();
    }

    private List<HeaderItemViewModel> getItems(ResourceResolver resourceResolver,
                                               HeaderResourceModel resourceModel) {
        return Optional.ofNullable(resourceModel.getLinks())
                .orElse(Collections.emptyList()).stream()
                .map(itemResourceModel -> getItemLink(itemResourceModel, resourceResolver))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    private Optional<HeaderItemViewModel> getItemLink(HeaderItemResourceModel itemResourceModel,
                                                      ResourceResolver resourceResolver) {
        return Optional.ofNullable(itemResourceModel.getLink())
                .map(resourceResolver::getResource)
                .map(r -> r.adaptTo(Page.class))
                .map(itemLinkPage -> HeaderItemViewModel.builder()
                        .title(getTitle(itemResourceModel, itemLinkPage))
                        .link(NavUtils.addHtmlExtension(itemLinkPage.getPath()))
                        .build());
    }

    private String getTitle(HeaderItemResourceModel itemResourceModel, Page page) {
        String title = itemResourceModel.getTitle();
        if (StringUtils.isNotBlank(title)) {
            return title;
        } else {
            return StringUtils.firstNonBlank(page.getNavigationTitle(),
                    page.getPageTitle(), page.getTitle());
        }
    }

    private HeaderViewModel createViewModelWithoutContent() {
        return HeaderViewModel.builder().hasContent(false).build();
    }
}
