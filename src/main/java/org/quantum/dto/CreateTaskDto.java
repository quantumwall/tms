package org.quantum.dto;

import org.quantum.entity.Task.Priority;

import jakarta.validation.constraints.NotBlank;

public record CreateTaskDto(@NotBlank String name, String description, Priority priority, Long responsibleId) {

}
