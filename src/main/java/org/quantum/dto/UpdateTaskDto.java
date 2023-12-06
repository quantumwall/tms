package org.quantum.dto;

import org.quantum.entity.Task.Priority;
import org.quantum.entity.Task.Status;

import jakarta.validation.constraints.NotNull;

public record UpdateTaskDto(@NotNull Long id, String name, String description, Status status, Priority priority,
		Long responsibleId) {

}
