package com.example.reminderActeAuto.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val mailSender: JavaMailSender,
    @Value("\${spring.mail.username}") private val fromEmail: String,
) {
    @Async
    fun sendExpiryEmail(to: String, vehicleBrand: String, docType: String, days: Int){
        val message = SimpleMailMessage()
        message.from = fromEmail
        message.setTo(to)
        message.subject = "Atentie, $docType expira in $days zile!"
        message.text = "Buna ziua,\n\n$docType pentru $vehicleBrand va expira in $days zile!\nAcest email a fost trimis automat de catre aplicatia unui proiect personal, non-profit, iar introducerea datelor personale este riscanta si nerecomandata!"
        mailSender.send(message)
    }

    @Async
    fun sendPasswordResetEmail(to: String, token: String){
        val message = SimpleMailMessage()
        message.from = fromEmail
        message.setTo(to)
        message.subject = "Resetare parola"
        message.text = "Codul tau pentru resetarea parolei este: $token\nAcest email a fost trimis automat de catre aplicatia unui proiect personal, non-profit, iar introducerea datelor personale este riscanta si nerecomandata!"
        mailSender.send(message)
    }

    @Async
    fun sendRegistrationEmail(to: String){
        val message = SimpleMailMessage()
        message.from = fromEmail
        message.setTo(to)
        message.subject = "Inregistrare $to"
        message.text = "Multumim pentru crearea contului pe platforma noastra, speram sa gasiti aceasta aplicatie utila\nAcest email a fost trimis automat de catre aplicatia unui proiect personal, non-profit, iar introducerea datelor personale este riscanta si nerecomandata!"
        mailSender.send(message)
    }
}