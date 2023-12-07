package org.quantum.controller.v1;

import java.security.Principal;

import org.quantum.dto.CreateTaskDto;
import org.quantum.dto.ReadTaskDto;
import org.quantum.dto.UpdateTaskDto;
import org.quantum.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

	private final TaskService taskService;

	@GetMapping
	public ResponseEntity<?> getAllUserTasks(@RequestParam(required = false) Long userId, Principal principal) {
		var tasks = taskService.findAllByUserId(userId, principal).stream().map(ReadTaskDto::from).toList();
		return ResponseEntity.status(HttpStatus.OK).body(tasks);
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody CreateTaskDto taskDto, Principal principal) {
		var createdTask = taskService.createTask(taskDto, principal);
		return ResponseEntity.status(HttpStatus.CREATED).body(ReadTaskDto.from(createdTask));

	}

	@PatchMapping
	public ResponseEntity<?> update(@RequestBody UpdateTaskDto taskDto, Principal principal) {
		var updatedTask = taskService.update(taskDto, principal);
		return ResponseEntity.status(HttpStatus.OK).body(ReadTaskDto.from(updatedTask));
	}
}
