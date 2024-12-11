package org.gol.fibworker.application;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
class FibJobDataDeserializer {

    private static final Pattern SERIALIZED_DATA_PATTERN = Pattern.compile("FIBONACCI\\((.*)\\) -> (.*)");


    FibJobData deserialize(String data) {
        var m = SERIALIZED_DATA_PATTERN.matcher(data);
        if (!m.find())
            throw new IllegalArgumentException("Cannot deserialize the fibonacci job details, format is not valid according to the contract: serializedData=" + data);
        return new FibJobData(Integer.valueOf(m.group(1)), m.group(2));
    }

    record FibJobData(Integer number, String algorithm) {
    }
}
