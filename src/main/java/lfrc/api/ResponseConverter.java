package lfrc.api;

import lfrc.model.Temperatures;

public interface ResponseConverter {
    String toResponse(Temperatures temps, String city);
}
