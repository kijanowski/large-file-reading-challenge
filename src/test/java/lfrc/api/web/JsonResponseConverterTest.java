package lfrc.api.web;

import lfrc.model.Reading;
import lfrc.model.Temperatures;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonResponseConverterTest {

    private final JsonResponseConverter sit = new JsonResponseConverter();

    @Test
    void withNullTemperatures() {
        // given
        var expected = "[]";

        // when
        String response = sit.toResponse(null, "city");

        // then
        assertEquals(expected, response);
    }

    @Test
    void withEmptyTemperatures() {
        // given
        var temps = new Temperatures();

        var expected = "[]";

        // when
        String response = sit.toResponse(temps, "city");

        // then
        assertEquals(expected, response);
    }

    @Test
    void withNullCity() {
        // given
        var temps = new Temperatures();
        temps.addReading(new Reading("city", "2020", 10.0));
        temps.addReading(new Reading("city", "2020", 20.0));
        temps.addReading(new Reading("city", "2021", 30.0));

        var expected = "[]";

        // when
        String response = sit.toResponse(temps, null);

        // then
        assertEquals(expected, response);
    }

    @Test
    void withOneCity() {
        // given
        var temps = new Temperatures();
        temps.addReading(new Reading("city", "2020", 10.0));
        temps.addReading(new Reading("city", "2020", 20.0));
        temps.addReading(new Reading("city", "2021", 30.0));

        var expected = """
                [{"year":"2020","averageTemperature":15.0},{"year":"2021","averageTemperature":30.0}]""";

        // when
        String response = sit.toResponse(temps, "city");

        // then
        assertEquals(expected, response);
    }

    @Test
    void withTwoCities() {
        // given
        var temps = new Temperatures();
        temps.addReading(new Reading("city1", "2020", 10.0));
        temps.addReading(new Reading("city1", "2020", 20.0));
        temps.addReading(new Reading("city1", "2021", 30.0));
        temps.addReading(new Reading("city2", "2020", 15.0));
        temps.addReading(new Reading("city2", "2020", 25.0));
        temps.addReading(new Reading("city2", "2021", 35.0));

        var expected = """
                [{"year":"2020","averageTemperature":15.0},{"year":"2021","averageTemperature":30.0}]""";

        // when
        String response = sit.toResponse(temps, "city1");

        // then
        assertEquals(expected, response);
    }

    @Test
    void withUnknownCity() {
        // given
        var temps = new Temperatures();
        temps.addReading(new Reading("city1", "2020", 10.0));
        temps.addReading(new Reading("city1", "2020", 20.0));
        temps.addReading(new Reading("city1", "2021", 30.0));

        var expected = "[]";

        // when
        String response = sit.toResponse(temps, "city2");

        // then
        assertEquals(expected, response);
    }

    @Test
    void sorted() {
        // given
        var temps = new Temperatures();
        temps.addReading(new Reading("city", "2021", 30.0));
        temps.addReading(new Reading("city", "2020", 10.0));
        temps.addReading(new Reading("city", "2020", 20.0));

        var expected = """
                [{"year":"2020","averageTemperature":15.0},{"year":"2021","averageTemperature":30.0}]""";

        // when
        String response = sit.toResponse(temps, "city");

        // then
        assertEquals(expected, response);
    }

}
