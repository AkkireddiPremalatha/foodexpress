package com.menu.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAvailabilityRequest {
	@NotNull(message = "Availability status cannot be null")
    private boolean isAvailable;
}
