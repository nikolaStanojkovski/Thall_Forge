package com.musicdistribution.thallforge.utils;

public final class NavUtils {

    public static String addHtmlExtension(String link) {
        if (link.endsWith(".html")) {
            return link;
        } else {
            return link + ".html";
        }
    }
}