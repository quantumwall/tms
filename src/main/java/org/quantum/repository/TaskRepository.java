package org.quantum.repository;

import java.util.List;

import org.quantum.entity.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {

    List<Task> findAllTasksByAuthorId(Long authorId);

    List<Task> findAllTasksByResponsibleId(Long responsibleId);
}
