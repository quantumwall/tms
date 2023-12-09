package org.quantum.service;

import java.util.Objects;
import java.util.Optional;

import org.quantum.dto.CreateTaskDto;
import org.quantum.dto.UpdateTaskDto;
import org.quantum.entity.Task;
import org.quantum.entity.User;
import org.quantum.exception.EntityNotFoundException;
import org.quantum.exception.InsufficientRightsException;
import org.quantum.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
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

	public Page<Task> findAllByUserId(Long userId, Integer page, Integer size) {
		var pageRequest = PageRequest.of(page, size);
		var searchUserId = userId;
		if (Objects.isNull(searchUserId)) {
			var principal = SecurityContextHolder.getContext().getAuthentication();
			searchUserId = userService.findByEmail(principal.getName()).get().getId();
		} else if (!userService.userExists(searchUserId)) {
			throw new EntityNotFoundException("User %d is not found".formatted(userId));
		}
		return taskRepository.findAllByAuthorOrResponsible(searchUserId, pageRequest);
	}

	public Optional<Task> findById(Long id) {
		return taskRepository.findById(id);
	}

	public boolean existsById(Long id) {
		return taskRepository.existsById(id);
	}

	@Transactional
	public Task createTask(CreateTaskDto createTaskDto) {
		var principal = SecurityContextHolder.getContext().getAuthentication();
		var author = userService.findByEmail(principal.getName()).get();
		var responsible = userService.findById(createTaskDto.responsibleId())
				.orElseThrow(() -> new EntityNotFoundException(
						"Responsible with id %d is not found".formatted(createTaskDto.responsibleId())));
		var task = Task.builder().name(createTaskDto.name()).description(createTaskDto.description())
				.status(Task.Status.IDLE).priority(createTaskDto.priority()).author(author).responsible(responsible)
				.build();
		return createTask(task);
	}

	@Transactional
	public Task createTask(Task task) {
		return taskRepository.save(task);
	}

	@Transactional
	public Task update(UpdateTaskDto taskDto) {
		var principal = SecurityContextHolder.getContext().getAuthentication();
		var user = userService.findByEmail(principal.getName()).get();
		var task = taskRepository.findById(taskDto.id())
				.orElseThrow(() -> new EntityNotFoundException("Task %d is not found".formatted(taskDto.id())));
		if (isAuthor(user, task)) {
			mapFrom(taskDto, task);
			if (Objects.nonNull(taskDto.responsibleId()) && task.getResponsible().getId() != taskDto.responsibleId()) {
				var newResponsible = userService.findById(taskDto.responsibleId())
						.orElseThrow(() -> new EntityNotFoundException(
								"Responsible with id %d is not found".formatted(taskDto.responsibleId())));
				task.setResponsible(newResponsible);
			}
			return task;
		}
		if (isResponsible(user, task)) {
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

	private boolean isAuthor(User user, Task task) {
		return task.getAuthor().getId().equals(user.getId());
	}

	private boolean isResponsible(User user, Task task) {
		return task.getResponsible().getId().equals(user.getId());
	}
}
