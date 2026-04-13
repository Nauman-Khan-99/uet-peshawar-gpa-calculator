package com.uetpeshawar.gpacalculator.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class CalculatorTest {

    @Test
    fun `calculateSemesterGpa returns weighted average`() {
        val courses = listOf(
            CourseInput(id = 1, creditHours = "3", grade = "A"),
            CourseInput(id = 2, creditHours = "4", grade = "B+")
        )

        val result = Calculator.calculateSemesterGpa(courses)

        assertEquals(7.0, result.totalCreditHours, 0.0001)
        assertEquals((3 * 4.0 + 4 * 3.33) / 7.0, result.value, 0.0001)
    }

    @Test
    fun `calculateSemesterGpa handles invalid inputs safely`() {
        val courses = listOf(
            CourseInput(id = 1, creditHours = "", grade = "A"),
            CourseInput(id = 2, creditHours = "abc", grade = "F")
        )

        val result = Calculator.calculateSemesterGpa(courses)

        assertEquals(0.0, result.totalCreditHours, 0.0001)
        assertEquals(0.0, result.value, 0.0001)
    }

    @Test
    fun `calculateCgpa returns weighted average and ignores invalid values`() {
        val semesters = listOf(
            SemesterInput(id = 1, creditHours = "15", gpa = "3.5"),
            SemesterInput(id = 2, creditHours = "17", gpa = "3.8"),
            SemesterInput(id = 3, creditHours = "", gpa = "4.0")
        )

        val result = Calculator.calculateCgpa(semesters)

        assertEquals(32.0, result.totalCreditHours, 0.0001)
        assertEquals((15 * 3.5 + 17 * 3.8) / 32.0, result.value, 0.0001)
    }
}
