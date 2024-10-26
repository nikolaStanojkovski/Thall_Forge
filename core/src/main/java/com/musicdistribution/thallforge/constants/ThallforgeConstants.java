package com.musicdistribution.thallforge.constants;

import java.util.Arrays;
import java.util.List;

public final class ThallforgeConstants {

    public static final class Endpoints {

        private static final String SERVLET_BASE_PATH = "/bin/thallforge";
        public static final String MUSIC_PLAYER_SHUFFLE_SELECTOR = "shuffle";
    }

    public static final class SqlQuery {

        public static final int DEFAULT_LIMIT = 10;
    }

    public static final class Date {

        public static final String DATE_FORMAT = "dd.MM.yyyy";
    }

    public static final class Extensions {

        public static final String JSON = "json";
        public static final String HTML = "html";
        public static final String TEXT = "txt";
        public static final String JPG = "jpg";
        public static final String MP3 = "mp3";
        public static final String WAV = "wav";
    }

    public static final class Names {

        public static final String ARTIST_DROPDOWN_OPTIONS = "artistDropdownOptions";
    }

    public static final class MimeTypes {

        public static final List<String> ALL_IMAGE_MIME_TYPES = Arrays.asList("image/png", "image/jpeg", "image/tiff",
                "image/png", "image/bmp", "image/gif", "image/pipeg", "image/x-portable-anymap",
                "image/x-portable-bitmap", "image/x-portable-graymap", "image/x-portable-pixmap",
                "image/x-rgb", "image/x-xbitmap", "image/x-xpixmap", "image/x-icon",
                "image/photoshop", "image/x-photoshop", "image/psd", "application/photoshop",
                "application/psd", "image/vnd.adobe.photoshop");
    }

    public static final class MetadataSchema {

        public static final String ALBUM_METADATA_SCHEMA = "/conf/global/settings/dam/adminui-extension/foldermetadataschema/album-schema";
    }

    public static final class ResourceTypes {

        public static final String MUSIC_PLAYER = "thallforge/components/content/musicplayer";
    }

    public static final class Paths {
        public static final String CONTENT = "/content";
        public static final String ETC = "/etc";
        public static final String CONTENT_DAM = CONTENT + "/dam";
        public static final String ETC_THALLFORGE = ETC + "/thallforge";
    }
}
