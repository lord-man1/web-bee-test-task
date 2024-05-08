package org.example;

import java.io.IOException;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(
                ZoneId.of("Asia/Yekaterinburg")
        );
        System.out.println(isWeekend(zonedDateTime));
    }

    public static boolean isWeekend(LocalDate date) {
        ProductionCalendarWeekends info = ProductionCalendarWeekends.loadCalendar(
                date.getYear()
        );
        Map<Integer, Set<String>> weekends = info.getYearWeekends();
        return weekends.get(date.getMonth().getValue()).contains(String.valueOf(date.getDayOfWeek().getValue()));
    }

    public static boolean isWeekend(ZonedDateTime zonedDateTime) {
        ZonedDateTime transferedTime = zonedDateTime.withZoneSameInstant(
                ZoneId.of("Europe/Moscow")
        );
        if (isWeekend(transferedTime.toLocalDate())) return true;
        return !transferedTime.toLocalTime().isAfter(LocalTime.of(9, 0))
                || !transferedTime.toLocalTime().isBefore(LocalTime.of(18, 0));
    }

}
