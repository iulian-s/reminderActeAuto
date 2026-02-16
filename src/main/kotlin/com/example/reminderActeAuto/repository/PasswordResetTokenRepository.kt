package com.example.reminderActeAuto.repository

import com.example.reminderActeAuto.model.PasswordResetToken
import com.example.reminderActeAuto.model.User
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface PasswordResetTokenRepository: JpaRepository<PasswordResetToken, Long> {

    @Query("""
       SELECT t FROM PasswordResetToken t
       JOIN FETCH t.user u
       WHERE
        t.tokenHash = :tokenHash 
        AND t.expiresAt > CURRENT_TIMESTAMP 
        AND t.used = FALSE
    """
    )
    fun findValidByTokenHash(@Param("tokenHash") tokenHash: String): PasswordResetToken?

    fun deleteByUsedTrueOrCreatedAtBefore(expiry: LocalDateTime)

    fun deleteByUser(user: User)

    @Modifying
    @Transactional
    @Query(
        "DELETE FROM PasswordResetToken t where t.used = true OR t.createdAt < :expiryTime"
    )
    fun deleteOldOrUsedTokens(@Param("expiryTime") expiry: LocalDateTime)
}