package org.quantum.dto;

import java.time.LocalDateTime;

import org.quantum.entity.Comment;

public record ReadCommentDto(Long id, LocalDateTime createdAt, String message, Long taskId, Long userId) {

	public static ReadCommentDto from(Comment comment) {
		return new ReadCommentDto(comment.getId(), comment.getCreatedAt(), comment.getMessage(),
				comment.getTask().getId(), comment.getUser().getId());
	}

}
