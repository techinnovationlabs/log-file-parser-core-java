package com.asahi.programing;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.log4j.receivers.varia.LogFilePatternReceiver;
import org.apache.log4j.spi.LoggingEvent;

public class LogFileParser extends LogFilePatternReceiver {

    private final List<LoggingEvent> events = new ArrayList<>();

    @Override
    public void doPost(LoggingEvent event) {
        events.add(event);
    }

    // A different way - suggested
    public Stream<LoggingEvent> parse(String pattern, Path file) {
        setFileURL(file.toUri().toString());
        setLogFormat(pattern);
        setTailing(false);
        setUseCurrentThread(true);
        initialize();
        activateOptions();
        return events.stream();
    }

    // Method asked to be developed
    public Stream<LoggingEvent> parse(String pattern, InputStream streamToLog) throws IOException {
        Path target = Paths.get("tempLogFile.log");
        Files.copy(streamToLog, target, StandardCopyOption.REPLACE_EXISTING);
        setFileURL(target.toUri().toString());
        setLogFormat(pattern);
        setTailing(false);
        setUseCurrentThread(true);
        initialize();
        activateOptions();
        Files.deleteIfExists(target);
        return events.stream();
    }
}