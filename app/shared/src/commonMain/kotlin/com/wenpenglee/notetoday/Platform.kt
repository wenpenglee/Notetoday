package com.wenpenglee.notetoday

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform