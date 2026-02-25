package com.example.reminderActeAuto.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Date

@Service
class JwtService {
    @Value("\${app.jwt.secret}")
    private lateinit var secret: String
    private val expirationTime = 86400000

    private fun getSignInKey() = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))

    fun generateToken(email: String): String{
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expirationTime))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    fun extractEmail(token: String): String{
        return extractClaim(token, Claims::getSubject)
    }

    fun isTokenValid(token: String): Boolean{
        return try{
            !extractExpiration(token).before(Date())
        } catch (e: Exception){
            false
        }
    }

    private fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T{
        val claims: Claims = Jwts.parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .body
        return claimsResolver(claims)
    }

    private fun extractExpiration(token: String): Date{
        return extractClaim(token, Claims::getExpiration)
    }
}