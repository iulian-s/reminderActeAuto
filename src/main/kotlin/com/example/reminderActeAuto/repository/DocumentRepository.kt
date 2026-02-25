package com.example.reminderActeAuto.repository

import com.example.reminderActeAuto.model.Document
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface DocumentRepository: JpaRepository<Document, Long> {

    @Query("""
        SELECT d from Document d
        JOIN FETCH d.vehicle v
        JOIN FETCH v.user u
        WHERE d.expiryDate > CURRENT_DATE
            AND (d.notification14Sent = false OR d.notification7Sent = false OR d.notification3Sent = false)
    """)
    fun findDocumentsNearExpiry(): List<Document>

}