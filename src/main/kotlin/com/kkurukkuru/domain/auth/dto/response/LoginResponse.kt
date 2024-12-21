package com.kkurukkuru.domain.auth.dto.response

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String
)