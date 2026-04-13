package com.uetpeshawar.gpacalculator.domain

data class CourseInput(
    val id: Long,
    val name: String = "",
    val creditHours: String = "",
    val grade: String = GradeScale.grades.first()
)

data class SemesterInput(
    val id: Long,
    val name: String = "",
    val creditHours: String = "",
    val gpa: String = ""
)

data class CalculationResult(
    val totalCreditHours: Double,
    val value: Double
)
