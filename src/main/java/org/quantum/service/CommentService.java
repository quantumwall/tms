package org.quantum.service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.quantum.dto.CreateCommentDto;
import org.quantum.entity.Comment;
import org.quantum.exception.EntityNotFoundException;
import org.quantum.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final TaskService taskService;
	private final UserService userService;

	@Transactional
	public Comment create(CreateCommentDto commentDto, Principal principal) {
		var task = taskService.findById(commentDto.taskId()).orElseThrow(
				() -> new EntityNotFoundException("Task with id %d is not found".formatted(commentDto.taskId())));
		var user = userService.findByEmail(principal.getName()).get();
		var comment = Comment.builder().user(user).task(task).message(commentDto.message())
				.createdAt(LocalDateTime.now()).build();
		return create(comment);
	}

	@Transactional
	public Comment create(Comment comment) {
		return commentRepository.save(comment);
	}

	public List<Comment> findAllByTaskId(Long id) {
		if (!taskService.existsById(id)) {
			throw new EntityNotFoundException("Task with id %d is not found".formatted(id));
		}
		return commentRepository.findAllByTaskId(id);
	}
}
