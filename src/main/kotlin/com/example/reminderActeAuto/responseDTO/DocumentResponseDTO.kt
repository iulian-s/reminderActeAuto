package com.example.reminderActeAuto.responseDTO

import java.time.LocalDate

data class DocumentResponseDTO(
    val id: Long,
    val vehicleId: Long,
    val type: String,
    val expiryDate: LocalDate,
    val notification14Sent: Boolean,
    val notification7Sent: Boolean,
    val notification3Sent: Boolean
)
