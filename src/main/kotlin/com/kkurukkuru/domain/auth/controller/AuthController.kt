package com.kkurukkuru.domain.auth.controller

import com.kkurukkuru.domain.auth.dto.request.LoginRequest
import com.kkurukkuru.domain.auth.dto.response.LoginResponse
import com.kkurukkuru.domain.auth.service.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): LoginResponse {
        return authService.login(request)
    }
}