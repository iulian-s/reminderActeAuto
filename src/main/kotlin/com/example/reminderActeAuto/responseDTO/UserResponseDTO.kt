package com.example.reminderActeAuto.responseDTO

import com.example.reminderActeAuto.model.Vehicle
import java.time.LocalDateTime

/**
 * Response DTO, what the API usually returns
 */
data class UserResponseDTO(
    val id: Long,
    val email: String,
    val isVerified: Boolean,
    val createdAt: LocalDateTime,
    val vehicles: MutableSet<VehicleResponseDTO> = mutableSetOf()
)
