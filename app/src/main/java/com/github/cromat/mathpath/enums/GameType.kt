package com.github.cromat.mathpath.enums

enum class GameType(private val gameType: String){
    ALL("all"),
    STEPS("steps"),
    TIME("time");

    override fun toString(): String {
        return gameType
    }
}