package io.github.ryunen344.kn.keyed

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform