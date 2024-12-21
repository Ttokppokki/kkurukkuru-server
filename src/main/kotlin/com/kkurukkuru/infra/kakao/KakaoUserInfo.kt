package com.kkurukkuru.infra.kakao

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoUserInfo(
    val id: Long,
    @JsonProperty("connected_at")
    val connectedAt: String,
    val properties: Properties,
    @JsonProperty("kakao_account")
    val kakaoAccount: KakaoAccount
) {
    data class Properties(
        val nickname: String,
        @JsonProperty("profile_image")
        val profileImage: String?,
        @JsonProperty("thumbnail_image")
        val thumbnailImage: String?
    )

    data class KakaoAccount(
        val email: String?,
        @JsonProperty("email_needs_agreement")
        val emailNeedsAgreement: Boolean?,
        @JsonProperty("is_email_valid")
        val isEmailValid: Boolean?,
        @JsonProperty("is_email_verified")
        val isEmailVerified: Boolean?,
        val profile: Profile?
    ) {
        data class Profile(
            val nickname: String,
            @JsonProperty("profile_image_url")
            val profileImageUrl: String?,
            @JsonProperty("thumbnail_image_url")
            val thumbnailImageUrl: String?
        )
    }
}