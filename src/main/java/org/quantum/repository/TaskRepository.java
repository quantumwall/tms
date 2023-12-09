package org.quantum.repository;

import org.quantum.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long> {

	@Query("select t from Task t where t.author.id = :id or t.responsible.id = :id")
	Page<Task> findAllByAuthorOrResponsible(@Param("id") Long id, Pageable page);

//	List<Task> findAllByResponsibleId(Long id);

}
