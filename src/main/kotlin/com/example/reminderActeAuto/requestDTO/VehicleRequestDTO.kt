package com.example.reminderActeAuto.requestDTO

import jakarta.validation.constraints.NotBlank

data class VehicleRequestDTO(
    var userId: Long,

    @field:NotBlank(message = "Please enter the car manufacturer")
    var brand: String,

    @field:NotBlank(message = "Please enter the car model")
    var model: String
)
