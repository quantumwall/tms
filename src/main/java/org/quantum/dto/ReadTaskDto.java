package org.quantum.dto;

import org.quantum.entity.Task;

public record ReadTaskDto(Long id, String name, String description, String status, String priority, ReadUserDto author,
		ReadUserDto responsible) {

	public static ReadTaskDto from(Task task) {
		return new ReadTaskDto(task.getId(), task.getName(), task.getDescription(), task.getStatus().name(),
				task.getPriority().name(), ReadUserDto.from(task.getAuthor()), ReadUserDto.from(task.getResponsible()));

	}
}
