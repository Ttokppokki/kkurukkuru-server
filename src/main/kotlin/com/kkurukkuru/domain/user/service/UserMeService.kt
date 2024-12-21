package com.kkurukkuru.domain.user.service

import com.kkurukkuru.domain.user.dto.response.UserMeResponse
import com.kkurukkuru.global.security.holder.SecurityHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserMeService(
    private val securityHolder: SecurityHolder
) {
    @Transactional
    fun getMe(): UserMeResponse {
        val user = securityHolder.user

        return UserMeResponse.of(user)
    }
}