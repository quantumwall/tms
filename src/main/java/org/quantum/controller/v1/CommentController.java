package org.quantum.controller.v1;

import org.quantum.dto.CreateCommentDto;
import org.quantum.dto.ReadCommentDto;
import org.quantum.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@GetMapping
	public ResponseEntity<?> getCommentsByTaskId(@RequestParam Long taskId) {
		var comments = commentService.findAllByTaskId(taskId).stream().map(ReadCommentDto::from).toList();
		return ResponseEntity.status(HttpStatus.OK).body(comments);
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody CreateCommentDto commentDto) {
		var createdComment = commentService.create(commentDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(ReadCommentDto.from(createdComment));
	}
}
