package com.musicdistribution.thallforge.components.content.artistprofiles;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class ArtistProfilesViewModel {

    private final String title;
    private final boolean hasContent;
}
