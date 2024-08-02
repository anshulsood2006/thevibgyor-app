package com.arsoft.projects.thevibgyor.common.util;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class TimeUtil {

    public static String UTC = "UTC";

    public static String getCurrentTime(ZonedDateTime startZonedDateTime, String zoneId) {
        log.info("getCurrentTime is called.");
        return startZonedDateTime.withZoneSameInstant(ZoneId.of(zoneId))
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public static long getElapsedTimeInMillis(ZonedDateTime startTime, ZonedDateTime endTime) {
        log.info("getElapsedTimeInMillis is called.");
        Duration duration = Duration.between(startTime, endTime);
        return duration.toMillis();
    }
}
