package com.github.cromat.mathpath.model

import com.github.cromat.mathpath.enums.GameType
import java.io.Serializable


class EquationConfig(var maxNumOperands: Int = 2,
                     var minNumOperands: Int = 2,
                     var maxNum: Int = 10,
                     var minNum: Int = 0,
                     var operators: List<String> = listOf<String>("+", "-"),
                     var gameType: String = GameType.STEPS.toString(),
                     var timeSec: Int = 60,
                     var stepsNum: Int = 10,
                     var negativeRes: Boolean = false,
                     var randomizeInput: Boolean = false,
                     var goldPerTask: Int = 1,
                     var braces: Boolean = false
) : Serializable
