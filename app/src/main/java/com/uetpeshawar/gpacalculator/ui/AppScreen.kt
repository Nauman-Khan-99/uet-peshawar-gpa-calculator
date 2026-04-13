package com.uetpeshawar.gpacalculator.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.ExposedDropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.uetpeshawar.gpacalculator.domain.Calculator
import com.uetpeshawar.gpacalculator.domain.CourseInput
import com.uetpeshawar.gpacalculator.domain.GradeScale
import com.uetpeshawar.gpacalculator.domain.SemesterInput
import java.util.Locale

private enum class AppTab(val title: String) {
    Gpa("GPA"),
    Cgpa("CGPA")
}

@Composable
fun GpaCgpaApp(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by rememberSaveable { mutableStateOf(AppTab.Gpa) }

    Scaffold(
        topBar = {
            Column {
                Text(
                    text = "UET Peshawar GPA Calculator",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
                TabRow(selectedTabIndex = selectedTab.ordinal) {
                    AppTab.entries.forEach { tab ->
                        Tab(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            text = { Text(tab.title) }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        when (selectedTab) {
            AppTab.Gpa -> {
                GpaScreen(
                    courses = uiState.courses,
                    onAddCourse = viewModel::addCourse,
                    onRemoveCourse = viewModel::removeCourse,
                    onCourseNameChanged = viewModel::updateCourseName,
                    onCourseCreditHoursChanged = viewModel::updateCourseCreditHours,
                    onCourseGradeChanged = viewModel::updateCourseGrade,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            AppTab.Cgpa -> {
                CgpaScreen(
                    semesters = uiState.semesters,
                    onAddSemester = viewModel::addSemester,
                    onRemoveSemester = viewModel::removeSemester,
                    onSemesterNameChanged = viewModel::updateSemesterName,
                    onSemesterCreditHoursChanged = viewModel::updateSemesterCreditHours,
                    onSemesterGpaChanged = viewModel::updateSemesterGpa,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun GpaScreen(
    courses: List<CourseInput>,
    onAddCourse: () -> Unit,
    onRemoveCourse: (Long) -> Unit,
    onCourseNameChanged: (Long, String) -> Unit,
    onCourseCreditHoursChanged: (Long, String) -> Unit,
    onCourseGradeChanged: (Long, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val result = Calculator.calculateSemesterGpa(courses)

    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(courses, key = { it.id }) { course ->
            CourseItem(
                course = course,
                onRemove = { onRemoveCourse(course.id) },
                onNameChanged = { onCourseNameChanged(course.id, it) },
                onCreditHoursChanged = { onCourseCreditHoursChanged(course.id, it) },
                onGradeChanged = { onCourseGradeChanged(course.id, it) }
            )
            HorizontalDivider()
        }

        item {
            Button(onClick = onAddCourse, modifier = Modifier.fillMaxWidth()) {
                Text("Add Course")
            }
        }

        item {
            Text(
                text = "Total Credit Hours: ${formatToTwoDecimals(result.totalCreditHours)}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "GPA: ${formatToTwoDecimals(result.value)}",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
private fun CgpaScreen(
    semesters: List<SemesterInput>,
    onAddSemester: () -> Unit,
    onRemoveSemester: (Long) -> Unit,
    onSemesterNameChanged: (Long, String) -> Unit,
    onSemesterCreditHoursChanged: (Long, String) -> Unit,
    onSemesterGpaChanged: (Long, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val result = Calculator.calculateCgpa(semesters)

    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(semesters, key = { it.id }) { semester ->
            SemesterItem(
                semester = semester,
                onRemove = { onRemoveSemester(semester.id) },
                onNameChanged = { onSemesterNameChanged(semester.id, it) },
                onCreditHoursChanged = { onSemesterCreditHoursChanged(semester.id, it) },
                onGpaChanged = { onSemesterGpaChanged(semester.id, it) }
            )
            HorizontalDivider()
        }

        item {
            Button(onClick = onAddSemester, modifier = Modifier.fillMaxWidth()) {
                Text("Add Semester")
            }
        }

        item {
            Text(
                text = "Total Credit Hours: ${formatToTwoDecimals(result.totalCreditHours)}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "CGPA: ${formatToTwoDecimals(result.value)}",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
private fun CourseItem(
    course: CourseInput,
    onRemove: () -> Unit,
    onNameChanged: (String) -> Unit,
    onCreditHoursChanged: (String) -> Unit,
    onGradeChanged: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Course",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Remove course")
            }
        }

        OutlinedTextField(
            value = course.name,
            onValueChange = onNameChanged,
            label = { Text("Course Name (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = course.creditHours,
            onValueChange = onCreditHoursChanged,
            label = { Text("Credit Hours") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        GradeDropdown(
            value = course.grade,
            onValueChanged = onGradeChanged,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun SemesterItem(
    semester: SemesterInput,
    onRemove: () -> Unit,
    onNameChanged: (String) -> Unit,
    onCreditHoursChanged: (String) -> Unit,
    onGpaChanged: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Semester",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Remove semester")
            }
        }

        OutlinedTextField(
            value = semester.name,
            onValueChange = onNameChanged,
            label = { Text("Semester Name/Number (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = semester.creditHours,
            onValueChange = onCreditHoursChanged,
            label = { Text("Semester Credit Hours") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = semester.gpa,
            onValueChange = onGpaChanged,
            label = { Text("Semester GPA") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GradeDropdown(
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text("Grade") },
            trailingIcon = { TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            GradeScale.grades.forEach { grade ->
                androidx.compose.material3.DropdownMenuItem(
                    text = { Text(grade) },
                    onClick = {
                        onValueChanged(grade)
                        expanded = false
                    }
                )
            }
        }
    }
}

private fun formatToTwoDecimals(value: Double): String =
    String.format(Locale.US, "%.2f", value)
