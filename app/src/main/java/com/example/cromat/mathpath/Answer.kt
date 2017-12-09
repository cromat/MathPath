package com.example.cromat.mathpath

import java.io.Serializable

class Answer(equation: String, userAns: String, rightAns: String) : Serializable {
    var equation = equation
    var userAns = userAns
    var rightAns = rightAns

    override fun toString(): String {
        return "blaaa"
    }
}