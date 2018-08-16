package com.github.cromat.mathpath.enums


enum class Range(private val range: String) {
    ALL("all"),
    WEEK("week"),
    MONTH("month");

    override fun toString(): String {
        return range
    }
}