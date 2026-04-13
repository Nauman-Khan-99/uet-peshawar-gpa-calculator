package com.uetpeshawar.gpacalculator.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uetpeshawar.gpacalculator.data.AppPreferencesRepository
import com.uetpeshawar.gpacalculator.domain.CourseInput
import com.uetpeshawar.gpacalculator.domain.GradeScale
import com.uetpeshawar.gpacalculator.domain.SemesterInput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MainUiState(
    val courses: List<CourseInput> = emptyList(),
    val semesters: List<SemesterInput> = emptyList()
)

class MainViewModel(
    private val repository: AppPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.state.collect { persistedState ->
                _uiState.update {
                    it.copy(
                        courses = persistedState.courses,
                        semesters = persistedState.semesters
                    )
                }
            }
        }
    }

    fun addCourse() {
        updateCourses { courses ->
            courses + CourseInput(id = nextCourseId(courses))
        }
    }

    fun removeCourse(id: Long) {
        updateCourses { courses ->
            courses.filterNot { it.id == id }
        }
    }

    fun updateCourseName(id: Long, value: String) {
        updateCourses { courses ->
            courses.map { if (it.id == id) it.copy(name = value) else it }
        }
    }

    fun updateCourseCreditHours(id: Long, value: String) {
        updateCourses { courses ->
            courses.map { if (it.id == id) it.copy(creditHours = value) else it }
        }
    }

    fun updateCourseGrade(id: Long, value: String) {
        updateCourses { courses ->
            courses.map {
                if (it.id == id) it.copy(grade = value.takeIf { grade -> grade in GradeScale.grades } ?: "A") else it
            }
        }
    }

    fun addSemester() {
        updateSemesters { semesters ->
            semesters + SemesterInput(id = nextSemesterId(semesters))
        }
    }

    fun removeSemester(id: Long) {
        updateSemesters { semesters ->
            semesters.filterNot { it.id == id }
        }
    }

    fun updateSemesterName(id: Long, value: String) {
        updateSemesters { semesters ->
            semesters.map { if (it.id == id) it.copy(name = value) else it }
        }
    }

    fun updateSemesterCreditHours(id: Long, value: String) {
        updateSemesters { semesters ->
            semesters.map { if (it.id == id) it.copy(creditHours = value) else it }
        }
    }

    fun updateSemesterGpa(id: Long, value: String) {
        updateSemesters { semesters ->
            semesters.map { if (it.id == id) it.copy(gpa = value) else it }
        }
    }

    private fun updateCourses(transform: (List<CourseInput>) -> List<CourseInput>) {
        val updated = transform(_uiState.value.courses)
        _uiState.update { it.copy(courses = updated) }
        viewModelScope.launch {
            repository.saveCourses(updated)
        }
    }

    private fun updateSemesters(transform: (List<SemesterInput>) -> List<SemesterInput>) {
        val updated = transform(_uiState.value.semesters)
        _uiState.update { it.copy(semesters = updated) }
        viewModelScope.launch {
            repository.saveSemesters(updated)
        }
    }

    private fun nextCourseId(courses: List<CourseInput>): Long = (courses.maxOfOrNull { it.id } ?: 0L) + 1L

    private fun nextSemesterId(semesters: List<SemesterInput>): Long = (semesters.maxOfOrNull { it.id } ?: 0L) + 1L
}

class MainViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository = AppPreferencesRepository(context)) as T
    }
}
