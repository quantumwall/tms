package org.quantum.controller.v1;

import org.quantum.dto.CreateUserDto;
import org.quantum.dto.JwtRequest;
import org.quantum.dto.JwtResponse;
import org.quantum.dto.ReadUserDto;
import org.quantum.exception.EntityAlreadyExistsException;
import org.quantum.service.UserService;
import org.quantum.util.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthorizeUserController {

	private final UserService userService;
	private final JwtUtils jwtUtils;
	private final AuthenticationManager authManager;

	@PostMapping("/authorize")
	public ResponseEntity<?> getJwtToken(@RequestBody JwtRequest request) {
		try {
			authManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
		} catch (BadCredentialsException e) {
			throw new EntityNotFoundException("Username or password are incorrect");
		}
		var principal = userService.loadUserByUsername(request.email());
		var jwtToken = jwtUtils.generateToken(principal);
		return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(jwtToken));
	}

	@PostMapping("/registration")
	public ResponseEntity<?> registerUser(@RequestBody CreateUserDto userDto) {
		if (userService.userExists(userDto.email())) {
			throw new EntityAlreadyExistsException("User %s is already exists".formatted(userDto.email()));
		}
		var createdUser = userService.create(userDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(ReadUserDto.from(createdUser));
	}

}
