package org.quantum.service;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.quantum.dto.CreateTaskDto;
import org.quantum.dto.UpdateTaskDto;
import org.quantum.entity.Task;
import org.quantum.exception.EntityNotFoundException;
import org.quantum.exception.InsufficientRightsException;
import org.quantum.repository.TaskRepository;
import org.quantum.security.SecurityUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TaskService {

	private final TaskRepository taskRepository;
	private final UserService userService;
//	private final Principal principal;

	public List<Task> findAllByUserId(Long userId, SecurityUser securityUser) {
		List<Task> tasks;
		if (Objects.isNull(userId)) {
			tasks = taskRepository.findAllByAuthorOrResponsible(securityUser.getId());
		} else {
			if (userService.userExists(userId)) {
				tasks = taskRepository.findAllByAuthorOrResponsible(userId);
			} else {
				throw new EntityNotFoundException("User %d is not found".formatted(userId));
			}
		}
		return tasks;
	}

	public Optional<Task> findById(Long id) {
		return taskRepository.findById(id);
	}

	@Transactional
	public Task createTask(CreateTaskDto createTaskDto, SecurityUser securityUser) {
		var authorId = securityUser.getId();
		var responsible = userService.findById(createTaskDto.responsibleId())
				.orElseThrow(() -> new EntityNotFoundException(
						"Responsible with id %d is not found".formatted(createTaskDto.responsibleId())));
		var author = userService.findById(authorId)
				.orElseThrow(() -> new EntityNotFoundException("User with id %d is not found".formatted(authorId)));
		var task = Task.builder().name(createTaskDto.name()).description(createTaskDto.description())
				.status(Task.Status.IDLE).priority(createTaskDto.priority()).author(author).responsible(responsible)
				.build();
		return createTask(task);
	}

	public Task createTask(Task task) {
		return taskRepository.save(task);
	}

	@Transactional
	public Task update(UpdateTaskDto taskDto, SecurityUser securityUser) {
		var authorId = securityUser.getId();
		var task = taskRepository.findById(taskDto.id())
				.orElseThrow(() -> new EntityNotFoundException("Task %d is not found".formatted(taskDto.id())));
		if (isAuthor(authorId, task)) {
			mapFrom(taskDto, task);
			if (taskDto.responsibleId() != null && task.getResponsible().getId() != taskDto.responsibleId()) {
				var newResponsible = userService.findById(taskDto.responsibleId())
						.orElseThrow(() -> new EntityNotFoundException(
								"Responsible with id %d is not found".formatted(taskDto.responsibleId())));
				task.setResponsible(newResponsible);
			}
			return task;
		}
		if (isResponsible(authorId, task)) {
			if (Objects.nonNull(taskDto.status())) {
				task.setStatus(taskDto.status());
				return task;
			} else {
				throw new EntityNotFoundException(
						"Responsible user only can modify status of the task. Status is not specified now");
			}
		}
		throw new InsufficientRightsException("Not enough rights to change the task %d".formatted(taskDto.id()));
	}

	private void mapFrom(UpdateTaskDto taskDto, Task task) {
		if (StringUtils.hasText(taskDto.name())) {
			task.setName(taskDto.name());
		}
		if (StringUtils.hasText(taskDto.description())) {
			task.setDescription(taskDto.description());
		}
		if (taskDto.status() != null) {
			task.setStatus(taskDto.status());
		}
		if (taskDto.priority() != null) {
			task.setPriority(taskDto.priority());
		}
	}

	private boolean isAuthor(Long userId, Task task) {
		return task.getAuthor().getId().equals(userId);
	}

	private boolean isResponsible(Long userId, Task task) {
		return task.getResponsible().getId().equals(userId);
	}
}
