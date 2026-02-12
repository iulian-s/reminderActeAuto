package com.example.reminderActeAuto.requestDTO

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class ForgotPasswordRequestDTO(
    @field:Email("Must be a valid email address!")
    @field:NotBlank
    var email: String
)
