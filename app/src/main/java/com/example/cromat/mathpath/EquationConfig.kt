package com.example.cromat.mathpath

import java.io.Serializable

class EquationConfig : Serializable {

    var MAX_NUM_OPERANDS = 2
    var MIN_NUM_OPERANDS = 2
    var MAX_NUM = 10
    var MIN_NUM = 0
    var OPERATORS = listOf<String>("+", "-")
    var GAME_TYPE = GameType.STEPS.toString()
    var TIME_SEC = 60
    var STEPS_NUM = 10

    constructor()
    
    constructor(MAX_NUM_OPERANDS: Int, MIN_NUM_OPERANDS: Int, MAX_NUM: Int, MIN_NUM: Int,
                OPERATORS: List<String>, GAME_TYPE: String, TIME_SEC: Int, STEPS_NUM: Int) : this(){
        this.MAX_NUM_OPERANDS = MAX_NUM_OPERANDS
        this.MIN_NUM_OPERANDS = MIN_NUM_OPERANDS
        this.MAX_NUM = MAX_NUM
        this.MIN_NUM = MIN_NUM
        this.OPERATORS = OPERATORS
        this.GAME_TYPE = GAME_TYPE
        this.TIME_SEC = TIME_SEC
        this.STEPS_NUM = STEPS_NUM
    }
}
