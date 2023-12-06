package org.quantum.service;

import java.util.List;
import java.util.Optional;

import org.quantum.dto.CreateUserDto;
import org.quantum.entity.User;
import org.quantum.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}

	@Transactional
	public User create(CreateUserDto createUserDto) {
		var user = User.builder().email(createUserDto.email()).name(createUserDto.name()).build();
		return create(user);
	}

	@Transactional
	public User create(User user) {
		log.info("user: {}", user);
		return userRepository.save(user);
	}
}
