package lfrc;

import lfrc.model.Reading;

import java.util.Optional;
import java.util.logging.Logger;

public class LineParser {

    private static final Logger LOG = Logger.getLogger(LineParser.class.getName());

    public Optional<Reading> parse(String line) {
        LOG.fine(() -> "Parsing line: " + line);
        var parts = line.split(";");
        if (parts.length != 3 || parts[1].length() != 23) { // naive timestamp validator 2019-09-19 05:17:32.619
            LOG.warning("Invalid line will be skipped: " + line);
            return Optional.empty();
        }

        var city = parts[0];
        var year = parts[1].substring(0, 4); // could be garbage

        try {
            var temperature = Double.parseDouble(parts[2]);
            return Optional.of(new Reading(city, year, temperature));
        } catch (NumberFormatException e) {
            LOG.warning("Invalid temperature will be skipped: " + line);
            return Optional.empty();
        }
    }
}
