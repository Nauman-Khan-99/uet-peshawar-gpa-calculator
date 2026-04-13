package com.uetpeshawar.gpacalculator.data

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore(name = "uet_gpa_calculator")
