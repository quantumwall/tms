package org.quantum.controller.v1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quantum.dto.ReadTaskDto;
import org.quantum.entity.Task;
import org.quantum.entity.User;
import org.quantum.exception.ControllerAdvisor;
import org.quantum.exception.EntityNotFoundException;
import org.quantum.service.TaskService;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

	@Mock
	TaskService taskService;

	@InjectMocks
	TaskController taskController;
	MockMvc mock;

	@BeforeEach
	void setUp() {
		mock = MockMvcBuilders.standaloneSetup(taskController).setControllerAdvice(new ControllerAdvisor()).build();
	}

	@Test
	void getAllUserTasksShouldThrowExceptionIfResponsibleNotExists() throws Exception {
		var errorMessage = "User 1 is not found";
		when(taskService.findAllByUserId(any(), any(), any())).thenThrow(new EntityNotFoundException(errorMessage));
		mock.perform(get("/api/v1/tasks").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound())
				.andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
				.andExpect(result -> assertEquals(errorMessage, result.getResolvedException().getMessage()));

	}

	@Test
	void getAllUserTasksShouldReturnValidResponseIfUserExists() throws Exception {
		var expectedBody = Map.of("tasks", getTasks().stream().map(ReadTaskDto::from).toList(), "currentPage", 0,
				"totalItems", 9L, "totalPages", 3);
		when(taskService.findAllByUserId(any(), any(), any()))
				.thenReturn(new PageImpl<Task>(getTasks(), PageRequest.of(0, 3), 9));

		var response = taskController.getAllUserTasks(1L, 0, 3);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isEqualTo(expectedBody);
	}

	List<Task> getTasks() {
		var user1 = new User(1L, "user1@user1", "user1", "123", true);
		var user2 = new User(2L, "user2@user2", "user2", "123", true);
		var task1 = new Task(1L, "task1", "description", Task.Status.IN_PROGRESS, Task.Priority.HIGH, user1, user2);
		var task2 = new Task(2L, "task2", "description", Task.Status.IN_PROGRESS, Task.Priority.HIGH, user2, user1);
		var task3 = new Task(3L, "task2", "description", Task.Status.IN_PROGRESS, Task.Priority.HIGH, user2, user1);
		var task4 = new Task(4L, "task2", "description", Task.Status.IN_PROGRESS, Task.Priority.HIGH, user2, user1);
		var task5 = new Task(5L, "task2", "description", Task.Status.IN_PROGRESS, Task.Priority.HIGH, user2, user1);
		return List.of(task1, task2, task3, task4, task5);
	}

}
