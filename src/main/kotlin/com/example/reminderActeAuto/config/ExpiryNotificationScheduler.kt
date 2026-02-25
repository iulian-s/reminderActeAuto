package com.example.reminderActeAuto.config

import com.example.reminderActeAuto.model.Document
import com.example.reminderActeAuto.model.User
import com.example.reminderActeAuto.repository.DocumentRepository
import com.example.reminderActeAuto.repository.PasswordResetTokenRepository
import com.example.reminderActeAuto.service.EmailService
import jakarta.transaction.Transactional
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Component
class ExpiryNotificationScheduler(
    private val documentRepository: DocumentRepository,
    private val emailService: EmailService,
    private val tokenRepository: PasswordResetTokenRepository
) {
    @Scheduled(cron = "0 0 1 * * *")
    @Transactional
    fun cleanupTokens(){
        val twentyFourHoursAgo = LocalDateTime.now().minusHours(24)
        tokenRepository.deleteOldOrUsedTokens(twentyFourHoursAgo)
    }

    @Scheduled(cron = "0 0 7 * * *")
    @Transactional
    fun checkDocumentExpirations(){
        val today = LocalDate.now()
        val documents = documentRepository.findDocumentsNearExpiry()
        for(doc in documents){
            val daysUntilExpiry = ChronoUnit.DAYS.between(today, doc.expiryDate)
            val user = doc.vehicle.user
            when {
                daysUntilExpiry <= 3 && !doc.notification3Sent -> {
                    sendAlert(user, doc, daysUntilExpiry)
                    doc.notification3Sent = true
                    doc.notification7Sent = true
                    doc.notification14Sent = true
                }
                daysUntilExpiry <= 7 && !doc.notification7Sent -> {
                    sendAlert(user, doc, daysUntilExpiry)
                    doc.notification7Sent = true
                    doc.notification14Sent = true
                }
                daysUntilExpiry <= 14 && !doc.notification14Sent -> {
                    sendAlert(user, doc, daysUntilExpiry)
                    doc.notification14Sent = true
                }
            }
        }
    }

    fun sendAlert(user: User, doc: Document, days: Long){
        val car = "${doc.vehicle.brand} ${doc.vehicle.model}"
        emailService.sendExpiryEmail(user.email, car, doc.type, days)
    }
}