package org.quantum.repository;

import java.util.List;
import java.util.Optional;

import org.quantum.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

	List<User> findAll();

	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);
}
