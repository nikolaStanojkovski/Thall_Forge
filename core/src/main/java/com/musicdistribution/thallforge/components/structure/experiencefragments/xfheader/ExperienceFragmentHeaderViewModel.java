package com.musicdistribution.thallforge.components.structure.experiencefragments.xfheader;

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
