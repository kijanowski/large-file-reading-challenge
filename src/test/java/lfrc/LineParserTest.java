package lfrc;

import lfrc.model.Reading;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LineParserTest {

    @Test
    void testParseValidLine() {
        // given
        var line = "London;2019-01-01 12:12:12.123;10.0";
        var expected = Optional.of(new Reading("London", "2019", 10.0));
        var parser = new LineParser();

        // when
        var reading = parser.parse(line);

        // then
        assertEquals(expected, reading);
    }

    @Test
    void testParseInvalidLineWithExtraField() {
        // given
        var line = "London;2019-01-01 12:12:12.123;10.0;extra";
        var parser = new LineParser();

        // when
        var reading = parser.parse(line);

        // then
        assertTrue(reading.isEmpty());
    }

    @Test
    void testParseInvalidLineWithInvalidTimestamp() {
        // given
        var line = "London;2019-01-01 12:12:12;10.0";
        var parser = new LineParser();

        // when
        var reading = parser.parse(line);

        // then
        assertTrue(reading.isEmpty());
    }

    @Test
    void testParseInvalidLineWithInvalidTemperature() {
        // given
        var line = "London;2019-01-01 12:12:12.123;10.0C";
        var parser = new LineParser();

        // when
        var reading = parser.parse(line);

        // then
        assertTrue(reading.isEmpty());
    }

    @Test
    void testParseInvalidLineWithMissingField() {
        // given
        var line = "London;2019-01-01 12:12:12.123";
        var parser = new LineParser();

        // when
        var reading = parser.parse(line);

        // then
        assertTrue(reading.isEmpty());
    }

    @Test
    void testParseInvalidLineWithEmptyLine() {
        // given
        var line = "";
        var parser = new LineParser();

        // when
        var reading = parser.parse(line);

        // then
        assertTrue(reading.isEmpty());
    }

    @Test
    void testParseInvalidLineWithNullLine() {
        // given
        var parser = new LineParser();

        // when
        var reading = parser.parse(null);

        // then
        assertTrue(reading.isEmpty());
    }
}
