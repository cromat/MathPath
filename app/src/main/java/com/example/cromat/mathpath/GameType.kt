package com.example.cromat.mathpath

enum class GameType(val gameType: String){
    STEPS("steps"),
    TIME("time");

    override fun toString(): String {
        return gameType
    }
}