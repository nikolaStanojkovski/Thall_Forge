package com.musicdistribution.thallforge.pages.xfheadervariation;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class ExperienceFragmentHeaderViewModel {

    private final String headerExperienceFragmentPath;
    private final boolean hasContent;
}
