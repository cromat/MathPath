package com.example.cromat.mathpath.enums


enum class Range(val range: String) {
    ALL("all"),
    WEEK("week"),
    MONTH("month");

    override fun toString(): String {
        return range
    }
}