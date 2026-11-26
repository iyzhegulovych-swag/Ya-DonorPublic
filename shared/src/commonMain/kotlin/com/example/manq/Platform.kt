package com.example.manq

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
