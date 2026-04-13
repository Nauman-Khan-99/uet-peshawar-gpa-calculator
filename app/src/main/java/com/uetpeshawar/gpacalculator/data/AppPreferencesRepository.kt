package com.uetpeshawar.gpacalculator.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.uetpeshawar.gpacalculator.domain.CourseInput
import com.uetpeshawar.gpacalculator.domain.SemesterInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

data class PersistedState(
    val courses: List<CourseInput> = emptyList(),
    val semesters: List<SemesterInput> = emptyList()
)

class AppPreferencesRepository(private val context: Context) {
    private object Keys {
        val courses = stringPreferencesKey("courses_json")
        val semesters = stringPreferencesKey("semesters_json")
    }

    val state: Flow<PersistedState> = context.dataStore.data.map { preferences ->
        PersistedState(
            courses = decodeCourses(preferences[Keys.courses]),
            semesters = decodeSemesters(preferences[Keys.semesters])
        )
    }

    suspend fun saveCourses(courses: List<CourseInput>) {
        context.dataStore.edit { preferences ->
            preferences[Keys.courses] = encodeCourses(courses)
        }
    }

    suspend fun saveSemesters(semesters: List<SemesterInput>) {
        context.dataStore.edit { preferences ->
            preferences[Keys.semesters] = encodeSemesters(semesters)
        }
    }

    private fun encodeCourses(courses: List<CourseInput>): String {
        val jsonArray = JSONArray()
        courses.forEach { course ->
            jsonArray.put(
                JSONObject()
                    .put("id", course.id)
                    .put("name", course.name)
                    .put("creditHours", course.creditHours)
                    .put("grade", course.grade)
            )
        }
        return jsonArray.toString()
    }

    private fun decodeCourses(raw: String?): List<CourseInput> {
        if (raw.isNullOrBlank()) return emptyList()
        return runCatching {
            val jsonArray = JSONArray(raw)
            buildList {
                for (index in 0 until jsonArray.length()) {
                    val item = jsonArray.optJSONObject(index) ?: continue
                    add(
                        CourseInput(
                            id = item.optLong("id", index.toLong()),
                            name = item.optString("name", ""),
                            creditHours = item.optString("creditHours", ""),
                            grade = item.optString("grade", "")
                                .takeIf { it.isNotBlank() } ?: "A"
                        )
                    )
                }
            }
        }.getOrDefault(emptyList())
    }

    private fun encodeSemesters(semesters: List<SemesterInput>): String {
        val jsonArray = JSONArray()
        semesters.forEach { semester ->
            jsonArray.put(
                JSONObject()
                    .put("id", semester.id)
                    .put("name", semester.name)
                    .put("creditHours", semester.creditHours)
                    .put("gpa", semester.gpa)
            )
        }
        return jsonArray.toString()
    }

    private fun decodeSemesters(raw: String?): List<SemesterInput> {
        if (raw.isNullOrBlank()) return emptyList()
        return runCatching {
            val jsonArray = JSONArray(raw)
            buildList {
                for (index in 0 until jsonArray.length()) {
                    val item = jsonArray.optJSONObject(index) ?: continue
                    add(
                        SemesterInput(
                            id = item.optLong("id", index.toLong()),
                            name = item.optString("name", ""),
                            creditHours = item.optString("creditHours", ""),
                            gpa = item.optString("gpa", "")
                        )
                    )
                }
            }
        }.getOrDefault(emptyList())
    }
}
