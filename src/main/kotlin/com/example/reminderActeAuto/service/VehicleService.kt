package com.example.reminderActeAuto.service

import com.example.reminderActeAuto.config.toResponseDTO
import com.example.reminderActeAuto.model.Vehicle
import com.example.reminderActeAuto.repository.UserRepository
import com.example.reminderActeAuto.repository.VehicleRepository
import com.example.reminderActeAuto.requestDTO.VehicleRequestDTO
import com.example.reminderActeAuto.responseDTO.UserResponseDTO
import com.example.reminderActeAuto.responseDTO.VehicleResponseDTO
import org.slf4j.LoggerFactory
import jakarta.transaction.Transactional
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service

@Service
class VehicleService(
    val vehicleRepository: VehicleRepository,
    val userRepository: UserRepository
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    fun getMyVehiclesAndDocuments(email: String): UserResponseDTO {
        return userRepository.findWithVehiclesAndDocumentsByEmail(email)?.toResponseDTO() ?: throw RuntimeException("No user found with email: $email")
    }

    @Transactional
    fun addVehicle(email: String, request: VehicleRequestDTO): VehicleResponseDTO {
        logger.info("Attempting to add new vehicle for user: {}", email)
        val user = userRepository.findByEmail(email) ?: throw RuntimeException("No user found with email: $email").also {
            logger.error("Failed to add vehicle: User {} not found", email)
        }
        val vehicle = Vehicle(
            user = user,
            brand = request.brand,
            model = request.model
        )
        user.addVehicle(vehicle)
        val savedVehicle = vehicleRepository.save(vehicle).toResponseDTO()
        logger.info("Successfully added vehicle with ID: {} for user: {}", savedVehicle.id, email)
        return savedVehicle
    }

    @Transactional
    fun deleteVehicle(email: String, vehicleId: Long){
        logger.warn("User {} is attempting to delete vehicle ID: {}", email, vehicleId)
        val vehicle = vehicleRepository.findById(vehicleId).orElseThrow{ Exception("No vehicle found with id: $vehicleId").also{
            logger.error("Delete failed: Vehicle ID {} not found", vehicleId)
        } }
        if(vehicle.user.email != email){
            logger.error("Security alert: User {} attempted to delete vehicle {} belonging to {}", email, vehicleId, vehicle.user.email)
            throw AccessDeniedException("Not your vehicle mate")
        }
        vehicleRepository.delete(vehicle)
        logger.info("Vehicle ID {} successfully deleted by {}", vehicleId, email)
    }

    @Transactional
    fun updateVehicle(email: String, vehicleId: Long, request: VehicleRequestDTO): VehicleResponseDTO {
        val vehicle = vehicleRepository.findById(vehicleId).orElseThrow{ Exception("No vehicle found with id: $vehicleId") }
        if(vehicle.user.email != email){
            throw AccessDeniedException("Not your vehicle mate")
        }
        logger.info("About to update vehicle {} for user: {}", vehicle.id, email)
        vehicle.brand = request.brand
        vehicle.model = request.model
        return vehicleRepository.save(vehicle).toResponseDTO().also{logger.info("Successfully updated vehicle with ID: {} for user {}", vehicle.id, email)}
    }


}