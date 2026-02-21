package com.example.reminderActeAuto.controller

import com.example.reminderActeAuto.requestDTO.ForgotPasswordRequestDTO
import com.example.reminderActeAuto.requestDTO.ResetPasswordDTO
import com.example.reminderActeAuto.requestDTO.UserRequestDTO
import com.example.reminderActeAuto.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/register")
    fun register(@Valid @RequestBody request: UserRequestDTO): ResponseEntity<String>{
        authService.register(request)
        return ResponseEntity.ok("Registration successful")
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: UserRequestDTO): ResponseEntity<AuthResponse>{
        val token = authService.login(request)
        return ResponseEntity.ok(AuthResponse(token))
    }

    @PostMapping("/reset-password")
    fun resetPassword(@Valid @RequestBody request: ResetPasswordDTO): ResponseEntity<Void>{
        authService.resetPassword(request)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/forgot-password")
    fun forgotPassword(@Valid @RequestBody request: ForgotPasswordRequestDTO): ResponseEntity<Void>{
        authService.forgotPassword(request)
        return ResponseEntity.ok().build()
    }
}

data class AuthResponse(val token: String){}