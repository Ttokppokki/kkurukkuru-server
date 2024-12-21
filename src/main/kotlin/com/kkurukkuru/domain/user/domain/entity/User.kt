package com.kkurukkuru.domain.user.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true)
    val kakaoId: Long,

    @Column(length = 100)
    var nickname: String,

    @Column(length = 500)
    var profileImage: String? = null,

    @Column(length = 100)
    var email: String? = null,

    @Column(name = "last_login")
    var lastLogin: LocalDateTime = LocalDateTime.now(),

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)