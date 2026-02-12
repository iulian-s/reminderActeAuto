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
import jakarta.persistence.Table
import java.time.LocalDate

@Table(
    name = "documents",
    indexes = [
        Index(name = "idx_document_expiry", columnList = "expiry_date"),
        Index(name = "idx_document_vehicle", columnList = "vehicle_id")
    ]
)
@Entity
class Document(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_id")
    var vehicle: Vehicle? = null,

    @Column(name = "type", nullable = true)
    var type: String? = null,

    @Column(name = "expiry_date", nullable = true)
    var expiryDate: LocalDate? = null,

    @Column(name = "notification_14_sent")
    var notification14Sent: Boolean = false,

    @Column(name = "notification_7_sent")
    var notification7Sent: Boolean = false,

    @Column(name = "notification_3_sent")
    var notification3Sent: Boolean = false
)
