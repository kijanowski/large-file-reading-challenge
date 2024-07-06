package lfrc.api.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lfrc.api.ResponseConverter;
import lfrc.model.Temp;
import lfrc.model.Temperatures;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

public class JsonResponseConverter implements ResponseConverter {

    private static final String EMPTY_BODY = "[]";
    private static final Logger LOG = Logger.getLogger(JsonResponseConverter.class.getName());

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String toResponse(Temperatures temps, String city) {
        return Optional.ofNullable(temps)
                .flatMap(temperatures -> temperatures.getFor(city))
                .map(this::convertToResponseModel)
                .map(this::convertToJson)
                .orElseGet(() -> {
                    LOG.info(() -> "No temperatures found for city: " + city);
                    return EMPTY_BODY;
                });
    }

    private List<TemperatureResponse> convertToResponseModel(Map<String, Temp> temperaturesForCity) {
        return temperaturesForCity
                .entrySet()
                .stream()
                .map(TemperatureResponse::of)
                .sorted(Comparator.comparing(TemperatureResponse::year))
                .toList();
    }

    private String convertToJson(List<TemperatureResponse> temperaturesForCity) {
        try {
            var jsonResponse = objectMapper.writeValueAsString(temperaturesForCity);
            LOG.fine(() -> "Converted to JSON: " + jsonResponse);
            return jsonResponse;
        } catch (JsonProcessingException jpe) {
            LOG.severe(() -> "Failed to convert to JSON: " + jpe.getMessage());
            return EMPTY_BODY;
        }
    }
}
