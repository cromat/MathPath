package com.example.cromat.mathpath.model

import org.mvel2.MVEL
import java.util.*

class Equation(private var eConfig: EquationConfig) {
    private var operands = listOf<String>()
    private var operators = listOf<String>()
    private var equationStr = ""
    private val rand = Random()
    private var result: Int? = null
    private var firstString = ""
    private var secondString = ""
    private var rightAns = ""

    init {
        generateEquation()
    }

    override fun toString(): String {
        return equationStr
    }

    fun toFullEquationString(): String {
        return "$equationStr=$result"
    }

    fun toUserEquationString(): String {
        if (eConfig.randomizeInput)
            return firstString + "_" + secondString
        return "$equationStr=_"
    }

    fun toStringList(): List<String> {
        var stringList = listOf<String>()
        for (i in 0 until operands.size) {
            stringList += operands[i]
            stringList += operators[i]
        }
        return stringList
    }

    fun getRightAns(): String {
        return rightAns
    }

    private fun generateEquation() {
        while (result == null || (!eConfig.negativeRes && result!! < 0)) {
            operands = listOf()
            operators = listOf()
            equationStr = ""
            result = 0
            var randNumOperands = eConfig.maxNumOperands
            if (eConfig.maxNumOperands > eConfig.minNumOperands) {
                randNumOperands = rand.nextInt(eConfig.maxNumOperands + 1
                        - eConfig.minNumOperands) + eConfig.minNumOperands
            }

            var operator = ""
            var randNum = 0
            for (i in 0 until randNumOperands) {
                if (operator == "/") {
                    val lastNum = randNum
                    randNum = 1
                    if (lastNum > 1) {
                        for (j in 2 until lastNum) {
                            if (lastNum % j == 0) {
                                randNum = j
                                break
                            }
                        }
                    }
                } else {
                    randNum = rand.nextInt(eConfig.maxNum - eConfig.minNum) + eConfig.minNum
                }
                operator = eConfig.operators[rand.nextInt(eConfig.operators.size)]
                equationStr += randNum.toString() + operator
                operands += randNum.toString()
                operators += operator
            }
            equationStr = equationStr.dropLast(1)
            operators = operators.dropLast(1) + ""
            val evaluated: Double = MVEL.eval("$equationStr.0") as Double
            result = evaluated.toInt()
            rightAns = result.toString()
        }
    }

    fun splitAtOperandIndex(index: Int = rand.nextInt(operands.size)): List<String> {
        var strings = listOf<String>()
        firstString = ""
        secondString += operators[index]
        rightAns = operands[index]

        for (i in 0 until index) {
            firstString += operands[i] + operators[i]
        }
        for (i in index + 1 until operands.size) {
            secondString += operands[i] + operators[i]
        }

        secondString += "=$result"
        strings += firstString
        strings += secondString
        return strings
    }

    fun isCorrect(number: String): Boolean {
        if (number.matches(Regex("^[0-9]*.\$"))) {
            if (eConfig.randomizeInput) {
                val eqToTry = (firstString + number + secondString).split("=")[0]
                val evaluated: Double = MVEL.eval("$eqToTry.0") as Double
                if (evaluated.toInt() == result) {
                    rightAns = number
                    return true
                }
            } else {
                val evaluated: Double = MVEL.eval("$equationStr.0") as Double
                if (evaluated.toInt() == result) {
                    rightAns = number
                    return true
                }
            }
        }
        return false
    }
}
