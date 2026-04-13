package com.uetpeshawar.gpacalculator.domain

object Calculator {
    fun calculateSemesterGpa(courses: List<CourseInput>): CalculationResult {
        var totalCreditHours = 0.0
        var totalQualityPoints = 0.0

        courses.forEach { course ->
            val creditHours = course.creditHours.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0
            val gradePoints = GradeScale.pointsByGrade[course.grade] ?: 0.0
            totalCreditHours += creditHours
            totalQualityPoints += creditHours * gradePoints
        }

        val gpa = if (totalCreditHours > 0.0) totalQualityPoints / totalCreditHours else 0.0
        return CalculationResult(totalCreditHours = totalCreditHours, value = gpa)
    }

    fun calculateCgpa(semesters: List<SemesterInput>): CalculationResult {
        var totalCreditHours = 0.0
        var weightedSum = 0.0

        semesters.forEach { semester ->
            val creditHours = semester.creditHours.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0
            val gpa = semester.gpa.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0
            totalCreditHours += creditHours
            weightedSum += creditHours * gpa
        }

        val cgpa = if (totalCreditHours > 0.0) weightedSum / totalCreditHours else 0.0
        return CalculationResult(totalCreditHours = totalCreditHours, value = cgpa)
    }
}
