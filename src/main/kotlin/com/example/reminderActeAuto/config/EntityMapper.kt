package com.example.reminderActeAuto.config

import com.example.reminderActeAuto.model.Document
import com.example.reminderActeAuto.model.User
import com.example.reminderActeAuto.model.Vehicle
import com.example.reminderActeAuto.requestDTO.DocumentRequestDTO
import com.example.reminderActeAuto.requestDTO.UserRequestDTO
import com.example.reminderActeAuto.requestDTO.VehicleRequestDTO
import com.example.reminderActeAuto.responseDTO.DocumentResponseDTO
import com.example.reminderActeAuto.responseDTO.UserResponseDTO
import com.example.reminderActeAuto.responseDTO.VehicleResponseDTO

fun User.toResponseDTO(): UserResponseDTO{
    val userId = this.id!!
    return UserResponseDTO(
        id = userId,
        email = this.email,
        isVerified = this.isVerified,
        createdAt = this.createdAt!!,
        vehicles = this.vehicles.map {it.toResponseDTO()}.toMutableSet()
    )
}

fun UserRequestDTO.toEntity(): User{
    return User(
        email = this.email,
        passwordHash = this.password
    )
}

fun VehicleRequestDTO.toEntity(user: User): Vehicle {
    return Vehicle(
        user = user,
        brand = this.brand,
        model = this.model,
    )
}

fun Vehicle.toResponseDTO(): VehicleResponseDTO {
    return VehicleResponseDTO(
        id = this.id!!,
        brand = this.brand,
        model = this.model,
        userId = this.user.id!!,
        createdAt = this.createdAt!!,
        documents = this.documents.map{it.toResponseDTO()}.toMutableSet()
    )
}

fun DocumentRequestDTO.toEntity(vehicle: Vehicle): Document {
    return Document(
        vehicle = vehicle,
        expiryDate = this.expiryDate,
        type = this.type
    )
}

fun Document.toResponseDTO(): DocumentResponseDTO {
    return DocumentResponseDTO(
        id = this.id!!,
        vehicleId = this.vehicle.id!!,
        type = this.type,
        expiryDate = this.expiryDate,
        notification14Sent = this.notification14Sent,
        notification7Sent = this.notification7Sent,
        notification3Sent = this.notification3Sent
    )
}