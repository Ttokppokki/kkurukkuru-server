package com.kkurukkuru.domain.auth.service

import com.kkurukkuru.domain.auth.dto.request.LoginRequest
import com.kkurukkuru.domain.auth.dto.response.LoginResponse
import com.kkurukkuru.domain.user.domain.entity.User
import com.kkurukkuru.domain.user.repository.UserRepository
import com.kkurukkuru.global.security.jwt.JwtProvider
import com.kkurukkuru.infra.kakao.KakaoTokenResponse
import com.kkurukkuru.infra.kakao.KakaoUserInfo
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.time.LocalDateTime

@Service
class AuthService(
    private val webClient: WebClient,
    private val jwtProvider: JwtProvider,
    private val userRepository: UserRepository,
    @Value("\${kakao.client-id}")
    private val clientId: String,
    @Value("\${kakao.client-secret}")
    private val clientSecret: String,
    @Value("\${kakao.redirect-uri}")
    private val redirectUri: String
) {
    @Transactional
    fun login(request: LoginRequest): LoginResponse {
        try {
            val kakaoToken = getKakaoToken(request.code)
            val userInfo = getKakaoUserInfo(kakaoToken.accessToken)
            val user = userRepository.findByKakaoId(userInfo.id)
                ?.apply {
                    nickname = userInfo.properties.nickname
                    profileImage = userInfo.properties.profileImage
                    email = userInfo.kakaoAccount.email
                    lastLogin = LocalDateTime.now()
                }
                ?: User(
                    kakaoId = userInfo.id,
                    nickname = userInfo.properties.nickname,
                    profileImage = userInfo.properties.profileImage,
                    email = userInfo.kakaoAccount.email
                )

            userRepository.save(user)

            val accessToken = jwtProvider.createAccessToken(user.id!!)
            val refreshToken = jwtProvider.createRefreshToken(user.id!!)

            return LoginResponse(
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("로그인에 실패했습니다.")
        }
    }

    private fun getKakaoToken(code: String): KakaoTokenResponse {
        return webClient.post()
            .uri("https://kauth.kakao.com/oauth/token") {
                it.queryParam("grant_type", "authorization_code")
                    .queryParam("client_id", clientId)
                    .queryParam("client_secret", clientSecret)
                    .queryParam("code", code)
                    .queryParam("redirect_uri", redirectUri)
                    .build()
            }
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .retrieve()
            .bodyToMono(KakaoTokenResponse::class.java)
            .block()!!
    }

    private fun getKakaoUserInfo(accessToken: String): KakaoUserInfo {
        return webClient.get()
            .uri("https://kapi.kakao.com/v2/user/me")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
            .retrieve()
            .bodyToMono(KakaoUserInfo::class.java)
            .block()!!
    }
}