package org.quantum.controller.v1;

import java.util.List;

import org.quantum.dto.CreateUserDto;
import org.quantum.entity.User;
import org.quantum.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody CreateUserDto createUserDto) {
        return ResponseEntity.ok(userService.create(createUserDto));
    }
    
//    @GetMapping("{id}/tasks")
//    public ResponseEntity<?> getUserTasks(@PathVariable Long id) {
//        return ResponseEntity.ok(userService.findUserTasks(id));
//    }

}
