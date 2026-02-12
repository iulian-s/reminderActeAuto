package com.example.reminderActeAuto.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Table(
    name = "vehicles",
    indexes = [
        Index(name = "idx_vehicle_user", columnList = "user_id")
    ]
)
@Entity
class Vehicle(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User,

    @Column(name = "brand")
    var brand: String,

    @Column(name = "model")
    var model: String,

    @CreationTimestamp
    @Column(name = "created_at")
    var createdAt: LocalDateTime? = null
){
    @OneToMany(mappedBy = "vehicle", cascade = [CascadeType.ALL], orphanRemoval = true)
    var documents: MutableSet<Document> = mutableSetOf()

    fun addDocument(document: Document){
        documents.add(document)
        document.vehicle = this
    }

    fun removeDocument(document: Document){
        documents.remove(document)
        document.vehicle = null
    }
}
