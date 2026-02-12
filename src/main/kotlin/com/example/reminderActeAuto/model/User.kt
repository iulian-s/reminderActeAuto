package com.example.reminderActeAuto.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Table(name = "users")
@Entity
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "email", unique = true, nullable = false)
    var email: String,

    @Column(name = "password_hash", nullable = false)
    var passwordHash: String,

    @Column(name = "is_verified", nullable = false)
    var isVerified: Boolean = false,

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    var createdAt: LocalDateTime? = null
){
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var vehicles: MutableSet<Vehicle> = mutableSetOf()

    fun addVehicle(vehicle: Vehicle){
        vehicles.add(vehicle)
        vehicle.user = this
    }

    fun removeVehicle(vehicle: Vehicle){
        vehicles.remove(vehicle)
        vehicle.user = null
    }
}
