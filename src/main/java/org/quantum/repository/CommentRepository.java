package org.quantum.repository;

import java.util.List;

import org.quantum.entity.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {

	List<Comment> findAllByTaskId(Long id);
}
