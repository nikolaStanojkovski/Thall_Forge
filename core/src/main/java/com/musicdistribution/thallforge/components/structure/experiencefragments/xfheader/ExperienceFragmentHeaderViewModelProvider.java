package com.musicdistribution.thallforge.components.structure.experiencefragments.xfheader;

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
public class ExperienceFragmentHeaderViewModelProvider implements ViewModelProvider<ExperienceFragmentHeaderViewModel> {

    @NonNull
    private final Page currentPage;

    @NonNull
    private final ResourceResolverRetrievalService resourceResolverRetrievalService;

    @Override
    public ExperienceFragmentHeaderViewModel getViewModel() {
        return Optional.ofNullable(currentPage.getContentResource())
                .map(contentResource -> contentResource.adaptTo(ExperienceFragmentHeaderResourceModel.class))
                .map(this::createViewModelWithContent)
                .orElseGet(this::createViewModelWithoutContent);
    }

    private ExperienceFragmentHeaderViewModel createViewModelWithContent(ExperienceFragmentHeaderResourceModel resourceModel) {
        return resourceResolverRetrievalService.getContentReaderResourceResolver()
                .map(resourceResolver -> resourceResolver.getResource(resourceModel.getHeaderExperienceFragmentReference()))
                .map(Resource::getPath)
                .map(xfPath -> ExperienceFragmentHeaderViewModel.builder()
                        .headerExperienceFragmentPath(xfPath)
                        .hasContent(true)
                        .build())
                .orElseGet(this::createViewModelWithoutContent);
    }

    private ExperienceFragmentHeaderViewModel createViewModelWithoutContent() {
        return ExperienceFragmentHeaderViewModel.builder()
                .hasContent(false)
                .build();
    }
}
