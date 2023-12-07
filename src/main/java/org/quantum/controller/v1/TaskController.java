package org.quantum.controller.v1;

import java.util.List;

import org.quantum.dto.CreateTaskDto;
import org.quantum.dto.UpdateTaskDto;
import org.quantum.entity.Task;
import org.quantum.security.SecurityUser;
import org.quantum.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
	public ResponseEntity<List<Task>> getAllUserTasks(@RequestParam(required = false) Long userId,
													  @AuthenticationPrincipal SecurityUser securityUser) {
		return ResponseEntity.status(HttpStatus.OK).body(taskService.findAllByUserId(userId, securityUser));
	}

	@PostMapping
	public ResponseEntity<Task> create(@RequestBody CreateTaskDto taskDto,
									   @AuthenticationPrincipal SecurityUser securityUser) {
		return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(taskDto, securityUser));

	}

	@PatchMapping
	public ResponseEntity<Task> update(@RequestBody UpdateTaskDto taskDto,
									   @AuthenticationPrincipal SecurityUser securityUser) {
		return ResponseEntity.status(HttpStatus.OK).body(taskService.update(taskDto, securityUser));
	}
}
