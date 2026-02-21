package com.example.reminderActeAuto.controller

import com.example.reminderActeAuto.requestDTO.VehicleRequestDTO
import com.example.reminderActeAuto.responseDTO.UserResponseDTO
import com.example.reminderActeAuto.responseDTO.VehicleResponseDTO
import com.example.reminderActeAuto.service.VehicleService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/vehicles")
class VehicleController (
    private val vehicleService: VehicleService
){
    @GetMapping
    fun getMyVehiclesAndDocuments(principal: Principal): ResponseEntity<UserResponseDTO> {
        return ResponseEntity.ok(vehicleService.getMyVehiclesAndDocuments(principal.name))
    }

    @PostMapping
    fun addVehicle(
        principal: Principal,
        @Valid @RequestBody request: VehicleRequestDTO
    ): ResponseEntity<VehicleResponseDTO> {
        return ResponseEntity.ok(vehicleService.addVehicle(principal.name, request))
    }

    @DeleteMapping("/{id}")
    fun deleteVehicle(principal: Principal, @PathVariable id:Long): ResponseEntity<Void> {
        vehicleService.deleteVehicle(principal.name, id)
        return ResponseEntity.noContent().build()
    }

    @PutMapping("/{id}")
    fun updateVehicle(
        principal: Principal,
        @PathVariable id: Long,
        @Valid @RequestBody request: VehicleRequestDTO
    ): ResponseEntity<VehicleResponseDTO> {
        return ResponseEntity.ok(vehicleService.updateVehicle(principal.name, id, request))
    }

}