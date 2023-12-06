package org.quantum.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.quantum.dto.CreateTaskDto;
import org.quantum.dto.UpdateTaskDto;
import org.quantum.entity.Task;
import org.quantum.entity.User;
import org.quantum.repository.TaskRepository;
import org.springframework.data.domain.Example;
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
    private final static User USER = new User(1L, "user1@user1.com", "user1");

    public List<Task> findAllByUserId(Long userId) {
	List<Task> tasks;
	if (Objects.isNull(userId)) {
	    tasks = taskRepository.findAllByAuthorOrResponsible(USER.getId());
	} else {
	    if (userService.userExists(userId)) {
		tasks = taskRepository.findAllByAuthorOrResponsible(userId);
	    } else {
		throw new RuntimeException();
	    }
	}
	return tasks;
    }

    public Optional<Task> findById(Long id) {
	return taskRepository.findById(id);
    }

    @Transactional
    public Task createTask(CreateTaskDto createTaskDto) {
	var responsible = userService.findById(createTaskDto.responsibleId())
		.orElseThrow(() -> new RuntimeException("Responsible not found"));
	var task = Task.builder().name(createTaskDto.name()).description(createTaskDto.description())
		.status(Task.Status.IDLE).priority(createTaskDto.priority()).author(USER).responsible(responsible)
		.build();
	return createTask(task);
    }

    public Task createTask(Task task) {
	return taskRepository.save(task);
    }

    @Transactional
    public Task update(UpdateTaskDto taskDto) {
	var task = taskRepository.findById(taskDto.id()).orElseThrow(RuntimeException::new);
	if (isAuthor(task)) {
	    mapFrom(taskDto, task);
	    if (taskDto.responsibleId() != null && task.getResponsible().getId() != taskDto.responsibleId()) {
		var newResponsible = userService.findById(taskDto.responsibleId()).orElseThrow(RuntimeException::new);
		task.setResponsible(newResponsible);
	    }
	    return task;
	}
	if (isResponsible(task)) {
	    if (taskDto.status() != null) {
		task.setStatus(taskDto.status());
		return task;
	    } else {
		throw new RuntimeException();
	    }
	}
	throw new RuntimeException();
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

    private boolean isAuthor(Task task) {
	return task.getAuthor().equals(USER);
    }

    private boolean isResponsible(Task task) {
	return task.getResponsible().equals(USER);
    }
}
