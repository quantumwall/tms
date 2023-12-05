package org.quantum.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserDto(@Email String email, @NotBlank String name) {

}
