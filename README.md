# UET Peshawar GPA & CGPA Calculator (Android)

Android app built with Kotlin + Jetpack Compose for calculating semester GPA and CGPA.

## Features

- **GPA screen**
  - Add/remove courses
  - Course fields: optional name, credit hours, and grade
  - Grade points mapping:
    - A = 4.00
    - A- = 3.67
    - B+ = 3.33
    - B = 3.00
    - B- = 2.67
    - C+ = 2.33
    - C = 2.00
    - D = 1.00
    - F = 0.00
  - GPA formula: `sum(creditHours * gradePoints) / sum(creditHours)`

- **CGPA screen (Option B)**
  - Add/remove semesters
  - Semester fields: optional semester name/number, semester credit hours, semester GPA
  - CGPA formula: `sum(semesterCreditHours * semesterGpa) / sum(semesterCreditHours)`

- **Persistence**
  - Uses AndroidX DataStore (Preferences)
  - Stores course and semester lists as JSON strings and restores on next launch

- **UI**
  - Material 3 styling
  - Top-level tab navigation between GPA and CGPA

- **Input safety**
  - Empty/invalid numeric inputs are treated as `0`
  - Division-by-zero safely returns `0.00`

## Build & Run

1. Open project in Android Studio (Hedgehog+ recommended).
2. Let Gradle sync complete.
3. Run the `app` configuration on an emulator/device.

## CLI verification

```bash
./gradlew testDebugUnitTest
./gradlew assembleDebug
```
