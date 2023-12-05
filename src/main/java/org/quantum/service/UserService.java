package org.quantum.service;

import java.util.Optional;

import org.quantum.entity.User;
import org.quantum.repository.UserRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
