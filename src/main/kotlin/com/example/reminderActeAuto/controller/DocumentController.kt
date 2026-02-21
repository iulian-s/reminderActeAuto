package com.example.reminderActeAuto.controller

import com.example.reminderActeAuto.requestDTO.DocumentRequestDTO
import com.example.reminderActeAuto.responseDTO.DocumentResponseDTO
import com.example.reminderActeAuto.service.DocumentService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import java.security.Principal

@RestController
@RequestMapping("/api/documents")
class DocumentController(
    private val documentService: DocumentService
) {
    @PostMapping("/vehicle/{vehicleId}")
    fun addDocument(
        @PathVariable vehicleId: Long,
        principal: Principal,
        @Valid @RequestBody request: DocumentRequestDTO): ResponseEntity<DocumentResponseDTO>{
        return ResponseEntity.ok(documentService.addDocument(principal.name, vehicleId, request))
    }

    @PutMapping("/{id}")
    fun updateDocument(
        @PathVariable id: Long,
        @Valid @RequestBody request: DocumentRequestDTO,
        principal: Principal
    ): ResponseEntity<DocumentResponseDTO>{
        return ResponseEntity.ok(documentService.updateDocument(principal.name, id, request))
    }

    @DeleteMapping("/{id}")
    fun deleteDocument(
        @PathVariable id: Long,
        principal: Principal
    ): ResponseEntity<Void>{
        documentService.deleteDocument(principal.name, id)
        return ResponseEntity.noContent().build()
    }
}