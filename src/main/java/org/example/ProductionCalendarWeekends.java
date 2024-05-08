package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public final class ProductionCalendarWeekends {
    private int year;
    private final Map<Integer, Set<String>> yearWeekends = new HashMap<>(12);

    private static final String CALENDAR_URL_FORMAT = "https://xmlcalendar.ru/data/ru/%d/calendar.json";
    private static final int MAX_CACHE_SIZE = 3;
    private static final LruCache<Integer, ProductionCalendarWeekends> LRU_CACHE = new LruCache<>(MAX_CACHE_SIZE);

    private ProductionCalendarWeekends() {
    }

    public static ProductionCalendarWeekends loadCalendar(int year) {
        if (LRU_CACHE.containsKey(year)) {
            return LRU_CACHE.get(year);
        }

        ProductionCalendarWeekends yearWeekends;
        try {
            URL url = new URL(String.format(CALENDAR_URL_FORMAT, year));
            yearWeekends = parseCalendarJson(url);
        } catch (IOException e) {
            throw new CalendarLoadingException("Could not load production calendar", e);
        }

        LRU_CACHE.put(year, yearWeekends);
        return yearWeekends;
    }

    private static ProductionCalendarWeekends parseCalendarJson(URL url) throws IOException {
        ProductionCalendarWeekends yearWeekends;
        ObjectMapper mapper = new ObjectMapper();
        yearWeekends = mapper.readValue(url, ProductionCalendarWeekends.class);
        return yearWeekends;

    }

    @JsonProperty("months")
    private void unpackNested(Map<String, String>[] months) {
        for (Map<String, String> month : months) {
            this.yearWeekends.put(
                    Integer.parseInt(month.get("month")),
                    Arrays.stream(month.get("days").split(","))
                            .filter(d -> !d.contains("*"))
                            .map(e -> e.contains("+") ? e.substring(0, e.length() - 1) : e)
                            .collect(Collectors.toSet()));
        }
    }
}
