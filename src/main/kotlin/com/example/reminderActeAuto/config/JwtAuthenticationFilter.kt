package com.example.reminderActeAuto.config

import com.example.reminderActeAuto.service.JwtService
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader: String? = request.getHeader("Authorization")
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }
        val jwt = authHeader.substring(7)
        try {
            val userEmail = jwtService.extractEmail(jwt)
            if(userEmail != null && SecurityContextHolder.getContext().authentication == null) {
                if(jwtService.isTokenValid(jwt)){
                    val authToken = UsernamePasswordAuthenticationToken(userEmail, null, listOf())
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                }
            }
        } catch (ex: ExpiredJwtException) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            return
        } catch (ex: Exception) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            return
        }
        filterChain.doFilter(request, response)
    }
}