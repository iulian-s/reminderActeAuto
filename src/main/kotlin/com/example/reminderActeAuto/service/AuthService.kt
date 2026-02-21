package com.example.reminderActeAuto.service

import com.example.reminderActeAuto.model.PasswordResetToken
import com.example.reminderActeAuto.model.User
import com.example.reminderActeAuto.repository.PasswordResetTokenRepository
import com.example.reminderActeAuto.repository.UserRepository
import com.example.reminderActeAuto.requestDTO.ChangePasswordRequestDTO
import com.example.reminderActeAuto.requestDTO.ForgotPasswordRequestDTO
import com.example.reminderActeAuto.requestDTO.ResetPasswordDTO
import com.example.reminderActeAuto.requestDTO.UserRequestDTO
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val tokenRepository: PasswordResetTokenRepository,
    private val emailService: EmailService,
) {
    fun register(request: UserRequestDTO){
        if(userRepository.findByEmail(request.email) != null) {
            throw RuntimeException("User already exists")
        }
        val hashedPassword = requireNotNull(passwordEncoder.encode(request.password)){
            "Password Encoder returned null!"
        }
        val user = User(
            email = request.email,
            passwordHash = hashedPassword,
            isVerified = false
        )
        userRepository.save(user)
        emailService.sendRegistrationEmail(request.email)
    }

    fun login(request: UserRequestDTO): String {
        val user = userRepository.findByEmail(request.email) ?: throw RuntimeException("User not found!")
        if(!passwordEncoder.matches(request.password, user.passwordHash)) throw RuntimeException("Password does not match!")
        return jwtService.generateToken(user.email)
    }

    @Transactional
    fun changePassword(email: String, request: ChangePasswordRequestDTO){
        val user = userRepository.findByEmail(email) ?: throw RuntimeException("User not found!")
        if(!passwordEncoder.matches(request.oldPassword, user.passwordHash)) throw RuntimeException("Old password is incorrect!")
        val hashedPassword = requireNotNull(passwordEncoder.encode(request.newPassword)) {
            "Password Encoder returned null for password change!"
        }
        user.passwordHash = hashedPassword
        userRepository.save(user)
    }

    @Transactional
    fun resetPassword(request: ResetPasswordDTO){
        val resetToken = tokenRepository.findValidByTokenHash(request.token) ?: throw RuntimeException("The token was used or expired!")
        val user = resetToken.user
        val hashedPassword = requireNotNull(passwordEncoder.encode(request.newPassword)) {
            "Password Encoder returned null for password change!"
        }
        user.passwordHash = hashedPassword
        resetToken.used = true
        userRepository.save(user)
        tokenRepository.save(resetToken)
    }

    @Transactional
    fun forgotPassword(request: ForgotPasswordRequestDTO){
        val user = userRepository.findByEmail(request.email) ?: throw RuntimeException("User not found!")
        tokenRepository.deleteByUser(user)
        val rawToken = UUID.randomUUID().toString()
        val hashedToken = requireNotNull(passwordEncoder.encode(rawToken)){
            "Password Encoder returned null! for token hashing"
        }
        val resetToken = PasswordResetToken(user = user, tokenHash = hashedToken, used = false, createdAt = LocalDateTime.now(), expiresAt = LocalDateTime.now().plusMinutes(15))
        tokenRepository.save(resetToken)
        emailService.sendPasswordResetEmail(user.email, rawToken)
    }
}