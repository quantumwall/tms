package org.quantum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCommentDto(@NotNull Long userId, @NotNull Long taskId, @NotBlank String message) {

}
