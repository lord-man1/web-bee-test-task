package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Calendar {
    private int year;
    private final Map<Integer, Set<String>> yearWeekends = new HashMap<>(12);

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
