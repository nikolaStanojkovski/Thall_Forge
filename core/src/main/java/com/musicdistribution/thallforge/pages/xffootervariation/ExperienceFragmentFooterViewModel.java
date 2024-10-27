package com.musicdistribution.thallforge.pages.xffootervariation;

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
