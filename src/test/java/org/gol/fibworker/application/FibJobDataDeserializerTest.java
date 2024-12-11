package org.gol.fibworker.application;

import org.assertj.core.api.Assertions;
import org.gol.fibworker.application.FibJobDataDeserializer.FibJobData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FibJobDataDeserializerTest {

    private final FibJobDataDeserializer sut = new FibJobDataDeserializer();

    @Test
    @DisplayName("should correctly deserialized the message body [positive]")
    void correctDeserialization() {
        //given
        var serialized = "FIBONACCI(45) -> ITERATIVE";

        //when
        var deserialized = sut.deserialize(serialized);

        //then
        assertThat(deserialized).isEqualTo(new FibJobData(45, "ITERATIVE"));
    }

    @Test
    @DisplayName("should throw exception for invalid message body [negative]")
    void wrongContract() {
        //given
        var serialized = "FIB(45) -> ITER";

        //when, then
        Assertions.assertThatThrownBy(() -> sut.deserialize(serialized))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("FIB(45) -> ITER");
    }
}