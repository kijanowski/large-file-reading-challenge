package lfrc.api.web;

import lfrc.model.Temp;

import java.util.Map;

public record TemperatureResponse(String year, double averageTemperature) {

    public static TemperatureResponse of(Map.Entry<String, Temp> mapEntry) {
        var average = mapEntry.getValue().average();
        var rounded = Math.round(average * 10.0) / 10.0;
        return new TemperatureResponse(mapEntry.getKey(), rounded);
    }
}
