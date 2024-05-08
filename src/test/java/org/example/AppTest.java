package org.example;

import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    private static final int WRONG_YEAR = -1234;
    private static final int YEAR = 2024;
    private static final int MONTH_OF_YEAR = 5;
    private static final int DAY_OF_MONTH = 9;
    private static final int HOUR = 15;
    private static final int MINUTE = 30;
    private static final String ZONE_ID = "Europe/Moscow";

    @Test
    void testCalendarLoading() {
        assertThrows(CalendarLoadingException.class,
                () -> ProductionCalendarWeekends.loadCalendar(WRONG_YEAR));
        assertDoesNotThrow(
                () -> ProductionCalendarWeekends.loadCalendar(YEAR));
    }

    @Test
    void testLocalDateWeekend() {
        LocalDate localDate = LocalDate.of(YEAR, MONTH_OF_YEAR, DAY_OF_MONTH);
        assertTrue(App.isWeekend(localDate));
        assertFalse(App.isWeekend(localDate.minusDays(1)));
    }

    @Test
    void testZoneDateTimeWeekend() {
        LocalDateTime localDateTime = LocalDateTime.of(
                YEAR, MONTH_OF_YEAR, DAY_OF_MONTH, HOUR, MINUTE
        ).minusDays(1);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of(ZONE_ID));
        assertTrue(App.isWeekend(zonedDateTime.plusHours(4)));
        assertFalse(App.isWeekend(zonedDateTime));
    }
}
