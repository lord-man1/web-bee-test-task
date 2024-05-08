package org.example.service;

import org.example.model.Calendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Set;

public abstract class CalendarService {
    public abstract Calendar loadCalendar(int year);

    public boolean isWeekend(Calendar calendar, LocalDate date) {
        Map<Integer, Set<String>> weekends = calendar.getYearWeekends();
        return weekends.get(date.getMonth().getValue()).contains(String.valueOf(date.getDayOfWeek().getValue()));
    }

    public boolean isWeekend(Calendar calendar, ZonedDateTime zonedDateTime) {
        ZonedDateTime transferedTime = zonedDateTime.withZoneSameInstant(
                ZoneId.of("Europe/Moscow")
        );
        if (isWeekend(calendar, transferedTime.toLocalDate())) return true;
        return !transferedTime.toLocalTime().isAfter(LocalTime.of(9, 0))
                || !transferedTime.toLocalTime().isBefore(LocalTime.of(18, 0));
    }
}
