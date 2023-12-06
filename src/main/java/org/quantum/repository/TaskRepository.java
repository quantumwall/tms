package org.quantum.repository;

import java.util.List;

import org.quantum.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long> {

	@Query("select t from Task t where t.author.id = :id or t.responsible.id = id")
	List<Task> findAllByAuthorIdOrResponsibleId(@Param("id") Long id);

//	List<Task> findAllByResponsibleId(Long id);

}
