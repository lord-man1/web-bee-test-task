package org.example;

import org.example.exception.CalendarLoadingException;
import org.example.model.Calendar;
import org.example.service.CalendarService;
import org.example.service.CalendarServiceWebJSON;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

public class CalendarServiceTest {

    static final int WRONG_YEAR = -1234;
    static final int YEAR = 2024;
    static final int MONTH_OF_YEAR = 5;
    static final int DAY_OF_MONTH = 9;
    static final int HOUR = 15;
    static final int MINUTE = 30;
    static final String ZONE_ID = "Europe/Moscow";
    static CalendarService calendarService;

    @BeforeAll
    static void initCalendarService() {
        calendarService = CalendarServiceWebJSON.getInstance();
    }


    @Test
    void testCalendarLoading() {
        assertThrows(CalendarLoadingException.class,
                () -> calendarService.loadCalendar(WRONG_YEAR));
        assertDoesNotThrow(
                () -> calendarService.loadCalendar(YEAR));
    }

    @Test
    void testLocalDateWeekend() {
        Calendar calendar = calendarService.loadCalendar(YEAR);
        LocalDate localDate = LocalDate.of(YEAR, MONTH_OF_YEAR, DAY_OF_MONTH);
        assertTrue(calendarService.isWeekend(calendar, localDate));
        assertFalse(calendarService.isWeekend(calendar, localDate.minusDays(1)));
    }

    @Test
    void testZoneDateTimeWeekend() {
        Calendar calendar = calendarService.loadCalendar(YEAR);
        LocalDateTime localDateTime = LocalDateTime.of(
                YEAR, MONTH_OF_YEAR, DAY_OF_MONTH, HOUR, MINUTE
        ).minusDays(1);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of(ZONE_ID));
        assertTrue(calendarService.isWeekend(calendar, zonedDateTime.plusHours(4)));
        assertFalse(calendarService.isWeekend(calendar, zonedDateTime));
    }
}
