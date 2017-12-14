package com.example.cromat.mathpath.enums

enum class GameType(val gameType: String){
    ALL("all"),
    STEPS("steps"),
    TIME("time");

    override fun toString(): String {
        return gameType
    }
}