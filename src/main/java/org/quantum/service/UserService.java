package org.quantum.service;

import java.util.List;
import java.util.Optional;

import org.quantum.dto.CreateUserDto;
import org.quantum.entity.User;
import org.quantum.repository.UserRepository;
import org.quantum.security.SecurityUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}

	public boolean userExists(Long userId) {
		return userRepository.existsById(userId);
	}

	public boolean userExists(String email) {
		return userRepository.existsByEmail(email);
	}

	@Transactional
	public User create(CreateUserDto createUserDto) {
		var user = User.builder().email(createUserDto.email()).name(createUserDto.name()).active(true)
				.password(passwordEncoder.encode(createUserDto.password())).build();
		return create(user);
	}

	@Transactional
	public User create(User user) {
		return userRepository.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByEmail(username).map(SecurityUser::new).orElseThrow(
				() -> new UsernameNotFoundException("User with email %s is not found".formatted(username)));
	}
}
