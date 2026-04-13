package com.uetpeshawar.gpacalculator.domain

object GradeScale {
    val pointsByGrade: Map<String, Double> = linkedMapOf(
        "A" to 4.00,
        "A-" to 3.67,
        "B+" to 3.33,
        "B" to 3.00,
        "B-" to 2.67,
        "C+" to 2.33,
        "C" to 2.00,
        "D" to 1.00,
        "F" to 0.00
    )

    val grades: List<String> = pointsByGrade.keys.toList()
}
