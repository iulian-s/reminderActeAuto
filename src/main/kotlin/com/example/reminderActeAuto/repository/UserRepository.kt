package com.example.reminderActeAuto.repository

import com.example.reminderActeAuto.model.User
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long> {
    @EntityGraph(attributePaths = ["vehicles", "vehicles.documents"]) //generates suitable joins to prevent N+1, useful for the dashboard
    fun findWithVehiclesAndDocumentsById(id: Long): User?

    fun findByEmail(email: String): User?
}