package lfrc.model;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Temperatures {

    private final Map<String, Map<String, Temp>> temps = new ConcurrentHashMap<>();

    public Optional<Map<String, Temp>> getFor(String city) {
        return Optional.ofNullable(city).map(temps::get);
    }

    public void addReading(Reading reading) {
        temps.putIfAbsent(reading.city(), new ConcurrentHashMap<>());
        temps.get(reading.city())
                .putIfAbsent(reading.year(), new Temp());
        temps
                .get(reading.city())
                .get(reading.year())
                .addReading(reading.temperature());
    }
}
