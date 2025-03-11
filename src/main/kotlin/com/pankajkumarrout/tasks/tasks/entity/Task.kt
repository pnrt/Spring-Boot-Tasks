package com.pankajkumarrout.tasks.tasks.entity

import jakarta.persistence.*
import lombok.NoArgsConstructor

@Entity
@Table(name = "tasks")
data class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val title: String = "",

    val description: String? = null
)
