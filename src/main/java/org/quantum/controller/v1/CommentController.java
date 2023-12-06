package org.quantum.controller.v1;

import java.util.List;

import org.quantum.dto.CreateCommentDto;
import org.quantum.entity.Comment;
import org.quantum.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@GetMapping("/{id}")
	public ResponseEntity<List<Comment>> getTaskComments(@PathVariable("id") Long taskId) {
		return ResponseEntity.status(HttpStatus.OK).body(commentService.findAllByTaskId(taskId));
	}

	@PostMapping
	public ResponseEntity<Comment> create(@RequestBody CreateCommentDto commentDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(commentService.create(commentDto));
	}
}
