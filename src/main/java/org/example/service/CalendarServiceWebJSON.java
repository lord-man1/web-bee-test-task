package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.exception.CalendarLoadingException;
import org.example.model.LruCache;
import org.example.model.Calendar;

import java.io.IOException;
import java.net.URL;

public class CalendarServiceWebJSON extends CalendarService {
    private static final String CALENDAR_URL_FORMAT = "https://xmlcalendar.ru/data/ru/%d/calendar.json";
    private static final int MAX_CACHE_SIZE = 3;
    private final LruCache<Integer, Calendar> lruCache = new LruCache<>(MAX_CACHE_SIZE);

    private CalendarServiceWebJSON() {
    }

    private static final class Holder {
        private static final CalendarServiceWebJSON INSTANCE = new CalendarServiceWebJSON();
    }

    public static CalendarService getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public Calendar loadCalendar(int year) {
        if (lruCache.containsKey(year)) {
            return lruCache.get(year);
        }

        Calendar yearWeekends;
        try {
            URL url = new URL(String.format(CALENDAR_URL_FORMAT, year));
            yearWeekends = parseCalendarJsonFrom(url);
        } catch (IOException e) {
            throw new CalendarLoadingException(
                    String.format("Production calendar for %d could not be loaded", year), e
            );
        }

        lruCache.put(year, yearWeekends);
        return yearWeekends;
    }

    private Calendar parseCalendarJsonFrom(URL url) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(url, Calendar.class);
    }
}
