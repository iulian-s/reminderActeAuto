package com.example.reminderActeAuto.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val mailSender: JavaMailSender,
) {
    @Async
    fun sendExpiryEmail(to: String, vehicleBrand: String, docType: String, days: Int){
        val message = SimpleMailMessage()
        message.setTo(to)
        message.subject = "Atentie, $docType expira in $days zile!"
        message.text = "Buna ziua,\n\n$docType pentru $vehicleBrand va expira in $days zile!"
        mailSender.send(message)
    }

    @Async
    fun sendPasswordResetEmail(to: String, token: String){
        val message = SimpleMailMessage()
        message.setTo(to)
        message.subject = "Resetare parola"
        message.text = "Codul tau pentru resetarea parolei este: $token"
        mailSender.send(message)
    }
}