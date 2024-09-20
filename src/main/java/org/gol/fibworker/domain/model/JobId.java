package org.gol.fibworker.domain.model;

import java.util.UUID;

import lombok.NonNull;

/**
 * Value Object representing job id.
 */
public record JobId(@NonNull UUID value) {
}
