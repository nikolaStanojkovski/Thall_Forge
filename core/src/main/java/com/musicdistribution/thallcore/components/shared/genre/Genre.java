package com.musicdistribution.thallcore.components.shared.genre;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Genre {
    ROCK("rock", "Rock"),
    METAL("metal", "Metal"),
    POP("pop", "Pop"),
    THALL("thall", "Thall"),
    RNB("rnb", "RnB"),
    RAP("rap", "Hip-Hop"),
    TRAP("trap", "Trap"),
    JAZZ("jazz", "Jazz");

    private final String name;
    private final String title;
}
