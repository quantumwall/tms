package org.quantum.service;

import org.quantum.dto.CreateTaskDto;
import org.quantum.entity.Task;
import org.quantum.entity.User;
import org.quantum.repository.TaskRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final static User user = new User(1L, "test@test", "test");

    public Task createTask(CreateTaskDto createTaskDto) {
        var responsible = userService.findById(createTaskDto.responsibleId())
            .orElseThrow(() -> new RuntimeException("Responsible not found"));
        var task = Task.builder().name(createTaskDto.name()).description(createTaskDto.description())
            .status(Task.Status.IDLE).priority(createTaskDto.priority()).author(user).responsible(responsible).build();
        return createTask(task);
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }
}
