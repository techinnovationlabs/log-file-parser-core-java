package com.asahi.programing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import org.apache.log4j.spi.LoggingEvent;

public class LogFileParserApplication {

    private static final String PATTERN_TYPE_1 = "TIMESTAMP [THREAD] LEVEL CLASS [] - MESSAGE";
    private static final String PATTERN_TYPE_2 = "[THREAD] LEVEL TIMESTAMP [CLASS:LINE] - MESSAGE";

    private static final String LOG_FILE_1 = "logFileFormat1.log";
    private static final String LOG_FILE_2 = "logFileFormat2.log";

    private static final String TIME_STAMP_FORMAT = "HH:mm:ss";

    public static void main(String[] args) {
        // Method asked to be developed
        try {
            parseLogFile(PATTERN_TYPE_1, LOG_FILE_1);
            parseLogFile(PATTERN_TYPE_2, LOG_FILE_2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // A different way - suggested
        try {
            parseLogFileAlternative(PATTERN_TYPE_1, LOG_FILE_1);
            parseLogFileAlternative(PATTERN_TYPE_2, LOG_FILE_2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void parseLogFile(String pattern, String fileName) throws IOException {
        LogFileParser parser = new LogFileParser();
        parser.setTimestampFormat(TIME_STAMP_FORMAT);
        FileInputStream fis = new FileInputStream(new File(fileName));
        Stream<LoggingEvent> events = parser.parse(pattern, fis);
        events.forEach(LogFileParserApplication::printEvent);
    }

    private static void parseLogFileAlternative(String pattern, String fileName) throws FileNotFoundException {
        LogFileParser parser = new LogFileParser();
        parser.setTimestampFormat(TIME_STAMP_FORMAT);
        Stream<LoggingEvent> events = parser.parse(pattern, Paths.get(fileName));
        events.forEach(LogFileParserApplication::printEvent);
    }

    public static void printEvent(LoggingEvent event) {
        StringBuilder builder = new StringBuilder();
        builder.append("========================>>>>>>>>>>>>>>>>>\n");
        builder.append(event.getTimeStamp() + "\n");
        builder.append(event.getThreadName() + "\n");
        builder.append(event.getLevel() + "\n");
        builder.append(event.getClass() + "\n");
        builder.append(event.getMessage() + "\n");

        Stream<String> stackTrace = Arrays.stream(event.getThrowableStrRep());
        stackTrace.forEach(str -> {
            builder.append(str + "\n");
        });

        builder.append("<<<<<<<<<<<<<<<<<<<===================");
        System.out.println(builder.toString());
    }
}