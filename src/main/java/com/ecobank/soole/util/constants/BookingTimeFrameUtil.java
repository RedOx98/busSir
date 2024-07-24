package com.ecobank.soole.util.constants;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class BookingTimeFrameUtil {
    private static final LocalTime EVENING_START = LocalTime.of(13, 0);
    private static final LocalTime EVENING_END = LocalTime.of(18, 59);
    private static final LocalTime MORNING_START = LocalTime.of(9, 0);
    private static final LocalTime MORNING_END = LocalTime.of(10, 00);

    public static boolean isMorning(LocalDateTime dateTime) {
        LocalTime time = dateTime.toLocalTime();
        return time.isAfter(EVENING_START) && time.isBefore(EVENING_END);
    }

    public static boolean isEvening(LocalDateTime dateTime) {
        LocalTime time = dateTime.toLocalTime();
        return time.isAfter(MORNING_START) && time.isBefore(MORNING_END);
    }
}
