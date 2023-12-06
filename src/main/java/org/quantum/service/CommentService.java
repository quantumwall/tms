package org.quantum.service;

import java.util.List;

import org.quantum.dto.CreateCommentDto;
import org.quantum.entity.Comment;
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
	public Comment create(CreateCommentDto commentDto) {
		var task = taskService.findById(commentDto.taskId()).orElseThrow(RuntimeException::new);
		var user = userService.findById(commentDto.userId()).orElseThrow(RuntimeException::new);
		var comment = Comment.builder().user(user).task(task).message(commentDto.message()).build();
		return create(comment);
	}

	@Transactional
	public Comment create(Comment comment) {
		return commentRepository.save(comment);
	}

	public List<Comment> findAllByTaskId(Long id) {
		return commentRepository.findAllByTaskId(id);
	}
}
