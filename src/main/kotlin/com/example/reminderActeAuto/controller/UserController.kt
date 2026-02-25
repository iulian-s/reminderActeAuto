package com.example.reminderActeAuto.controller

import com.example.reminderActeAuto.requestDTO.ChangePasswordRequestDTO
import com.example.reminderActeAuto.requestDTO.ForgotPasswordRequestDTO
import com.example.reminderActeAuto.requestDTO.ResetPasswordDTO
import com.example.reminderActeAuto.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/users")
class UserController(
    private val authService: AuthService
) {
    @PostMapping("/change-password")
    fun changePassword(@Valid @RequestBody request: ChangePasswordRequestDTO, principal: Principal): ResponseEntity<Void>{
        authService.changePassword(principal.name, request)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping
    fun deleteAccount(
        principal: Principal,
        @RequestParam inputPassword: String
    ): ResponseEntity<Void>{
        authService.deleteAccount(principal.name, inputPassword)
        return ResponseEntity.ok().build()
    }
}