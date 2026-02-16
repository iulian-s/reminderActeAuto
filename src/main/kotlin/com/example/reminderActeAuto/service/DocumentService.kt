package com.example.reminderActeAuto.service

import com.example.reminderActeAuto.config.toResponseDTO
import com.example.reminderActeAuto.model.Document
import com.example.reminderActeAuto.repository.DocumentRepository
import com.example.reminderActeAuto.repository.VehicleRepository
import com.example.reminderActeAuto.requestDTO.DocumentRequestDTO
import com.example.reminderActeAuto.responseDTO.DocumentResponseDTO
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service

@Service
class DocumentService(
    val vehicleRepository: VehicleRepository,
    val documentRepository: DocumentRepository
) {
    @Transactional
    fun addDocument(email: String, vehicleId: Long, request: DocumentRequestDTO): DocumentResponseDTO {
        val vehicle = vehicleRepository.findById(vehicleId).orElseThrow{ Exception("No vehicle found with id: $vehicleId") }
        if(vehicle.user.email != email){
            throw AccessDeniedException("Not your vehicle mate")
        }
        val doc = Document(
            vehicle = vehicle,
            type = request.type,
            expiryDate = request.expiryDate
        )
        vehicle.addDocument(doc)
        return documentRepository.save(doc).toResponseDTO()
    }

    @Transactional
    fun deleteDocument(email: String, docId: Long){
        val doc = documentRepository.findById(docId)
            .orElseThrow{ EntityNotFoundException("No document found with id: $docId") }
        if(doc.vehicle.user.email != email){
            throw AccessDeniedException("Not your vehicle mate")
        }
        documentRepository.delete(doc)
    }

    @Transactional
    fun updateDocument(email: String, docId: Long, request: DocumentRequestDTO): DocumentResponseDTO {
        val doc = documentRepository.findById(docId)
            .orElseThrow{ EntityNotFoundException("No document found with id: $docId") }
        if(doc.vehicle.user.email != email){
            throw AccessDeniedException("Not your vehicle mate")
        }
        doc.type = request.type
        doc.expiryDate = request.expiryDate
        return documentRepository.save(doc).toResponseDTO()
    }
}