package lfrc;

import lfrc.model.Reading;

import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Logger;

public class LineParser {

    private static final Logger LOG = Logger.getLogger(LineParser.class.getName());
    private static final boolean VALID = true;
    private static final boolean INVALID = false;

    public Optional<Reading> parse(String line) {
        LOG.fine(() -> "Parsing line: " + line);
        return Optional.ofNullable(line)
                .map(l -> l.split(";"))
                .filter(this::isValidLine)
                .filter(this::isValidTimestamp)
                .flatMap(this::createReading);
    }

    private Optional<Reading> createReading(String[] parts) {
        var city = parts[0];
        var year = parts[1].substring(0, 4); // could be smarter than that
        return extractTemperature(parts)
                .map(temperature -> new Reading(city, year, temperature));
    }

    private Optional<Float> extractTemperature(String[] parts) {
        try {
            return Optional.of(Float.parseFloat(parts[2]));
        } catch (NumberFormatException e) {
            LOG.warning("Invalid temperature will be skipped: " + parts[2]);
            return Optional.empty();
        }
    }

    private boolean isValidLine(String[] parts) {
        if (parts.length == 3) {
            return VALID;
        } else {
            LOG.warning("Invalid line does not contain all parts and will be skipped: " + Arrays.toString(parts));
            return INVALID;
        }
    }

    private boolean isValidTimestamp(String[] parts) {
        if (parts[1].length() == 23) { // naive timestamp validator "2019-09-19 05:17:32.619"
            return VALID;
        } else {
            LOG.warning("Invalid timestamp will be skipped: " + parts[1]);
            return INVALID;
        }
    }
}
