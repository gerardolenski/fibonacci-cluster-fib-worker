package org.gol.fibworker.domain.model;

import java.util.UUID;

import lombok.NonNull;

/**
 * Value Object representing task id.
 */
public record TaskId(@NonNull UUID value) {
}
