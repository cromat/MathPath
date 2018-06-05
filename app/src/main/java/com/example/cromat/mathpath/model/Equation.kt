package com.example.cromat.mathpath.model

import android.content.Context
import com.example.cromat.mathpath.DbHelper
import org.mvel2.MVEL
import java.util.*

class Equation(private var eConfig: EquationConfig, private val ctx: Context) {
    private var operands = listOf<String>()
    private var operators = listOf<String>()
    private var equationStr = ""
    private val rand = Random()
    private var result: Int? = null
    private var firstString = ""
    private var secondString = ""
    private var rightAns = ""
    private var braceOpen = -1
    private var braceClose = -1

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

            braceOpen = -1
            braceClose = -1

            if (eConfig.braces && rand.nextInt(2) == 1) {
                braceOpen = rand.nextInt(randNumOperands - 1)
                braceClose = rand.nextInt(randNumOperands - braceOpen - 1) + 1 + braceOpen
            }

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

                // Choose operator by percentage solving
                val perByOperators = DbHelper.getPercentageByOperation(ctx)
                val mapPerByOp = mutableMapOf<String, Int>()
                mapPerByOp["+"] = perByOperators[0]
                mapPerByOp["-"] = perByOperators[1]
                mapPerByOp["/"] = perByOperators[2]
                mapPerByOp["*"] = perByOperators[3]

                val sortedPerByOp = mapPerByOp.toList()
                        .sortedBy { (_, value) -> value }.toMap()

                operator = "+"
                val foo = rand.nextInt(401 - sortedPerByOp.values.sum())
                var sum = 0
                for (key in sortedPerByOp.keys) {
                    sum += 100 - sortedPerByOp[key]!!.toInt()
                    if (foo < sum) {
                        operator = key
                        break
                    }
                }

                // Check division by zero
                if (equationStr.isNotEmpty() && equationStr[equationStr.length - 1] == '/'
                        && randNum == 0)
                    randNum = 1

                if (i == braceOpen)
                    equationStr += '('
                equationStr += randNum.toString()
                if (i == braceClose)
                    equationStr += ')'
                equationStr += operator
                operands += randNum.toString()
                operators += operator
            }
            equationStr = equationStr.dropLast(1)
            operators = operators.dropLast(1) + ""
            val evaluated: Double = MVEL.eval(equationStr.replace(operands[0],
                    operands[0] + ".0")) as Double
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
            if (i == braceOpen)
                firstString += '('
            firstString += operands[i]
            if (i == braceClose)
                firstString += ')'
            firstString += operators[i]
        }

        if (index == braceOpen)
            firstString += '('

        if (index == braceClose)
            secondString += ')'

        for (i in index + 1 until operands.size) {
            if (i == braceOpen && i != 0)
                secondString += '('
            secondString += operands[i]
            if (i == braceClose)
                secondString += ')'
            secondString += operators[i]
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
                val evaluated: Double = MVEL.eval(eqToTry.replace(operands[0],
                        operands[0] + ".0")) as Double
                if (evaluated.toInt() == result) {
                    rightAns = number
                    return true
                }
            } else {
                val evaluated: Double = MVEL.eval("$equationStr.0") as Double
                if (evaluated.toInt() == number.toInt()) {
                    rightAns = number
                    return true
                }
            }
        }
        return false
    }
}
