package org.gol.fibworker.domain.model;

import static java.util.Optional.ofNullable;

public record FailureMessage(String value) {

    private static final String DEFAULT_ERR_MSG = "calculation failed";

    @Override
    public String value() {
        return ofNullable(value).orElse(DEFAULT_ERR_MSG);
    }
}
