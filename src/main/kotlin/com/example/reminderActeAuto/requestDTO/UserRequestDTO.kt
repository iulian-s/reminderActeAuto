package com.example.reminderActeAuto.requestDTO

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

/**
 * Request DTO used for login/registration
 */
data class UserRequestDTO(
    @field:Email(message = "Not a valid email address")
    @field:NotBlank
    var email: String,

    @field:NotBlank(message = "Password is required")
    var password: String
)
