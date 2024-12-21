package com.kkurukkuru.domain.user.dto.response

import com.kkurukkuru.domain.user.domain.entity.User
import java.time.LocalDateTime
import java.util.UUID

data class UserMeResponse(
    val id: Long,
    val kakaoId: Long,
    val nickname: String,
    val profileImage: String?,
    val email: String?,
    val lastLogin: LocalDateTime
) {
    companion object {
        fun of(user: User) = UserMeResponse(
            id = user.id!!,
            kakaoId = user.kakaoId,
            nickname = user.nickname,
            profileImage = user.profileImage,
            email = user.email,
            lastLogin = user.lastLogin
        )
    }
}