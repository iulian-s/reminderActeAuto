package com.example.reminderActeAuto.requestDTO

import jakarta.validation.constraints.Future
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

data class DocumentRequestDTO(
    var vehicleId: Long,

    @field:NotBlank(message = "Please enter what the type is")
    var type: String,

    @field:Future(message = "Expiry date must be in the future")
    var expiryDate: LocalDate
)
