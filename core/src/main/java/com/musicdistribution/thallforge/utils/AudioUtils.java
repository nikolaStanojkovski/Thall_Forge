package com.musicdistribution.thallforge.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.audio.AudioParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Slf4j
public final class AudioUtils {

    public static Integer getDuration(InputStream audioStream) {
        try {
            Parser parser = new AudioParser();
            Metadata metadata = new Metadata();
            parser.parse(audioStream, new BodyContentHandler(), metadata, new ParseContext());
            return Optional.ofNullable(metadata.get("duration"))
                    .map(Integer::parseInt)
                    .orElse(0);
        } catch (IOException | SAXException | TikaException e) {
            log.error("Could not read duration from audio stream", e);
        }
        return -1;
    }
}
