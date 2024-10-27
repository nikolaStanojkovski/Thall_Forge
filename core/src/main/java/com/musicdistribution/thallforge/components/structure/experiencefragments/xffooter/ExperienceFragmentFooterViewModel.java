package com.musicdistribution.thallforge.components.structure.experiencefragments.xffooter;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class ExperienceFragmentFooterViewModel {

    private final String footerExperienceFragmentPath;
    private final boolean hasContent;
}
