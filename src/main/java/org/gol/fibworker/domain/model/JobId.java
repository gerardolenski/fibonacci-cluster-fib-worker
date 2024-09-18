package org.gol.fibworker.domain.model;

import java.util.UUID;

import lombok.NonNull;

public record JobId(@NonNull UUID value) {
}
