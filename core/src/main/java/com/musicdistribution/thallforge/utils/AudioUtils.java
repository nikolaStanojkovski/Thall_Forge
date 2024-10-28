package com.musicdistribution.thallforge.utils;

import com.day.cq.dam.api.Asset;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.audio.AudioParser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
public final class AudioUtils {

    public static Integer getDurationSeconds(InputStream audioStream) {
        try {
            Parser parser = new Mp3Parser();
            Metadata metadata = new Metadata();
            parser.parse(audioStream, new BodyContentHandler(), metadata, new ParseContext());
            return Optional.ofNullable(metadata.get("xmpDM:duration"))
                    .map(Double::parseDouble)
                    .map(Double::intValue)
                    .orElse(0);
        } catch (IOException | SAXException | TikaException e) {
            log.error("Could not read duration from audio stream", e);
        }
        return -1;
    }

    public static boolean isAudioAsset(Asset asset) {
        List<String> audioMimeTypes = Arrays.asList("audio/aac", "audio/mpeg", "audio/ogg", "audio/wav");
        String mimeType = asset.getMimeType();
        if (StringUtils.isBlank(mimeType)) {
            return false;
        }
        return audioMimeTypes.contains(mimeType);
    }
}
