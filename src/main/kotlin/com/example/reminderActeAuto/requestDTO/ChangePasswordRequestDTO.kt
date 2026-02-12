package com.example.reminderActeAuto.requestDTO

import jakarta.validation.constraints.NotBlank

data class ChangePasswordRequestDTO(
    @field:NotBlank("Old password cannot be blank!")
    var oldPassword: String,

    @field:NotBlank("New password cannot be blank!")
    var newPassword: String
)
