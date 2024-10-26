package com.musicdistribution.thallforge.components.structure.footer;

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
public class FooterViewModelProvider implements ViewModelProvider<FooterViewModel> {

    @NonNull
    private final Resource resource;

    @NonNull
    private final ResourceResolverRetrievalService resourceResolverRetrievalService;

    @Override
    public FooterViewModel getViewModel() {
        return Optional.ofNullable(resource.adaptTo(FooterResourceModel.class))
                .flatMap(resourceModel -> resourceResolverRetrievalService.getContentReaderResourceResolver()
                        .map(resourceResolver -> createViewModel(resourceResolver, resourceModel)))
                .orElseGet(this::createViewModelWithoutContent);
    }

    private FooterViewModel createViewModel(ResourceResolver resourceResolver,
                                            FooterResourceModel resourceModel) {
        List<FooterItemViewModel> items = getItems(resourceResolver, resourceModel);
        return FooterViewModel.builder()
                .items(items)
                .hasContent(!items.isEmpty())
                .build();
    }

    private List<FooterItemViewModel> getItems(ResourceResolver resourceResolver,
                                               FooterResourceModel resourceModel) {
        return Optional.ofNullable(resourceModel.getLinks())
                .orElse(Collections.emptyList()).stream()
                .map(itemResourceModel -> getItemLink(itemResourceModel, resourceResolver))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    private Optional<FooterItemViewModel> getItemLink(FooterItemResourceModel itemResourceModel,
                                                      ResourceResolver resourceResolver) {
        return Optional.ofNullable(itemResourceModel.getLink())
                .map(resourceResolver::getResource)
                .map(r -> r.adaptTo(Page.class))
                .map(itemLinkPage -> FooterItemViewModel.builder()
                        .title(getTitle(itemResourceModel, itemLinkPage))
                        .link(NavUtils.addHtmlExtension(itemLinkPage.getPath()))
                        .build());
    }

    private String getTitle(FooterItemResourceModel itemResourceModel, Page page) {
        String title = itemResourceModel.getTitle();
        if (StringUtils.isNotBlank(title)) {
            return title;
        } else {
            return StringUtils.firstNonBlank(page.getNavigationTitle(),
                    page.getPageTitle(), page.getTitle());
        }
    }

    private FooterViewModel createViewModelWithoutContent() {
        return FooterViewModel.builder().hasContent(false).build();
    }
}
