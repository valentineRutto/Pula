package com.valentinerutto.orbmotion

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform