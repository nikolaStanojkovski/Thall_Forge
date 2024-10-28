package com.musicdistribution.thallforge.components.structure.experiencefragments.xffooter;

import com.day.cq.wcm.api.Page;
import com.musicdistribution.thallforge.components.ViewModelProvider;
import com.musicdistribution.thallforge.services.ResourceResolverRetrievalService;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.resource.Resource;

import java.util.Optional;

@Slf4j
@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ExperienceFragmentFooterViewModelProvider implements ViewModelProvider<ExperienceFragmentFooterViewModel> {

    @NonNull
    private final Page currentPage;

    @NonNull
    private final ResourceResolverRetrievalService resourceResolverRetrievalService;

    @Override
    public ExperienceFragmentFooterViewModel getViewModel() {
        return Optional.ofNullable(currentPage.getContentResource())
                .map(contentResource -> contentResource.adaptTo(ExperienceFragmentFooterResourceModel.class))
                .map(this::createViewModelWithContent)
                .orElseGet(this::createViewModelWithoutContent);
    }

    private ExperienceFragmentFooterViewModel createViewModelWithContent(ExperienceFragmentFooterResourceModel resourceModel) {
        return resourceResolverRetrievalService.getContentReaderResourceResolver()
                .map(resourceResolver -> resourceResolver.getResource(resourceModel.getFooterExperienceFragmentReference()))
                .map(Resource::getPath)
                .map(xfPath -> ExperienceFragmentFooterViewModel.builder()
                        .footerExperienceFragmentPath(xfPath)
                        .hasContent(true)
                        .build())
                .orElseGet(this::createViewModelWithoutContent);
    }

    private ExperienceFragmentFooterViewModel createViewModelWithoutContent() {
        return ExperienceFragmentFooterViewModel.builder()
                .hasContent(false)
                .build();
    }
}
