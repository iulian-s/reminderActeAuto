package com.example.reminderActeAuto.service

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.Refill
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

@Service
class RateLimitService {
    private val cache = ConcurrentHashMap<String, Bucket>()

    private fun createNewBucket(): Bucket {
        val limit = Bandwidth.classic(3, Refill.intervally(3, Duration.ofMinutes(15)))
        return Bucket.builder().addLimit(limit).build()
    }

    fun resolveBucket(email: String): Bucket {
        return cache.computeIfAbsent(email) { createNewBucket() }
    }
}