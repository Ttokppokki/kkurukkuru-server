package com.kkurukkuru.global.security.holder

import com.kkurukkuru.domain.user.domain.entity.User
import com.kkurukkuru.domain.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class SecurityHolder(
    private val userRepository: UserRepository
) {
    val user: User
        get() = userRepository.findByIdOrNull(SecurityContextHolder.getContext().authentication.name.toLong()) ?: throw IllegalStateException("User not found")
}