package com.example.reminderActeAuto.repository

import com.example.reminderActeAuto.model.Document
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DocumentRepository: JpaRepository<Document, Long> {
}