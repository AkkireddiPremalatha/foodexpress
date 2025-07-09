package com.foodexpress.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
	
@NotNull(message = "Email is mandatory")
@Email(message = "Invalid email format")
private String email;
@NotNull(message = "password is mandatory")

private String password;

}
