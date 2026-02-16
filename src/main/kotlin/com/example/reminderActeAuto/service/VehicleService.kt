package com.example.reminderActeAuto.service

import com.example.reminderActeAuto.config.toResponseDTO
import com.example.reminderActeAuto.model.Vehicle
import com.example.reminderActeAuto.repository.UserRepository
import com.example.reminderActeAuto.repository.VehicleRepository
import com.example.reminderActeAuto.requestDTO.VehicleRequestDTO
import com.example.reminderActeAuto.responseDTO.UserResponseDTO
import com.example.reminderActeAuto.responseDTO.VehicleResponseDTO
import jakarta.transaction.Transactional
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service

@Service
class VehicleService(
    val vehicleRepository: VehicleRepository,
    val userRepository: UserRepository
) {
    fun getMyVehiclesAndDocuments(email: String): UserResponseDTO {
        return userRepository.findWithVehiclesAndDocumentsByEmail(email)?.toResponseDTO() ?: throw RuntimeException("No user found with email: $email")
    }

    @Transactional
    fun addVehicle(email: String, request: VehicleRequestDTO): VehicleResponseDTO {
        val user = userRepository.findByEmail(email) ?: throw RuntimeException("No user found with email: $email")
        val vehicle = Vehicle(
            user = user,
            brand = request.brand,
            model = request.model
        )
        user.addVehicle(vehicle)
        return vehicleRepository.save(vehicle).toResponseDTO()
    }

    @Transactional
    fun deleteVehicle(email: String, vehicleId: Long){
        val vehicle = vehicleRepository.findById(vehicleId).orElseThrow{ Exception("No vehicle found with id: $vehicleId") }
        if(vehicle.user.email != email){
            throw AccessDeniedException("Not your vehicle mate")
        }
        vehicleRepository.delete(vehicle)
    }

    @Transactional
    fun updateVehicle(email: String, vehicleId: Long, request: VehicleRequestDTO): VehicleResponseDTO {
        val vehicle = vehicleRepository.findById(vehicleId).orElseThrow{ Exception("No vehicle found with id: $vehicleId") }
        if(vehicle.user.email != email){
            throw AccessDeniedException("Not your vehicle mate")
        }
        vehicle.brand = request.brand
        vehicle.model = request.model
        return vehicleRepository.save(vehicle).toResponseDTO()
    }


}