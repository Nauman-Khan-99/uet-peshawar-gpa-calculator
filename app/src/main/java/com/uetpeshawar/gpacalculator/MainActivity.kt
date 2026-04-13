package com.uetpeshawar.gpacalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uetpeshawar.gpacalculator.ui.GpaCgpaApp
import com.uetpeshawar.gpacalculator.ui.MainViewModel
import com.uetpeshawar.gpacalculator.ui.MainViewModelFactory
import com.uetpeshawar.gpacalculator.ui.theme.UetPeshawarGpaCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UetPeshawarGpaCalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: MainViewModel = viewModel(
                        factory = MainViewModelFactory(applicationContext)
                    )
                    GpaCgpaApp(viewModel = viewModel)
                }
            }
        }
    }
}
