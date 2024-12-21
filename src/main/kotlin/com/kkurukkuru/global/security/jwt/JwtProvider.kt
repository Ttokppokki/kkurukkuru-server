package com.kkurukkuru.global.security.jwt

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.Date

@Component
class JwtProvider(
    @Value("\${jwt.secret}")
    private val secretKey: String,
    @Value("\${jwt.access-token-validity}")
    private val accessTokenValidityInMilliseconds: Long,
    @Value("\${jwt.refresh-token-validity}")
    private val refreshTokenValidityInMilliseconds: Long
) {
    private val key: Key

    init {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        key = Keys.hmacShaKeyFor(keyBytes)
    }

    fun createAccessToken(userId: Long): String {
        val now = Date()
        val validity = Date(now.time + accessTokenValidityInMilliseconds)

        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

    fun createRefreshToken(userId: Long): String {
        val now = Date()
        val validity = Date(now.time + refreshTokenValidityInMilliseconds)

        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

    fun getUserId(token: String): Long {
        return try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body

            claims.subject.toLong()
        } catch (e: Exception) {
            throw InvalidTokenException("Invalid JWT token")
        }
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
            true
        } catch (e: SecurityException) {
            throw InvalidTokenException("Invalid JWT signature")
        } catch (e: MalformedJwtException) {
            throw InvalidTokenException("Invalid JWT token")
        } catch (e: ExpiredJwtException) {
            throw ExpiredTokenException("Expired JWT token")
        } catch (e: UnsupportedJwtException) {
            throw InvalidTokenException("Unsupported JWT token")
        } catch (e: IllegalArgumentException) {
            throw InvalidTokenException("JWT claims string is empty")
        }
    }
}

// exception/TokenExceptions.kt
class InvalidTokenException(message: String) : RuntimeException(message)
class ExpiredTokenException(message: String) : RuntimeException(message)