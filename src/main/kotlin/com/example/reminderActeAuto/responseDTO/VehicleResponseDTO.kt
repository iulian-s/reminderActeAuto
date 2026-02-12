package com.example.reminderActeAuto.responseDTO

import java.time.LocalDateTime

data class VehicleResponseDTO(
    val id: Long,
    val userId: Long,
    val brand: String,
    val model: String,
    val createdAt: LocalDateTime,
    val documents: MutableSet<DocumentResponseDTO> = mutableSetOf()
)
