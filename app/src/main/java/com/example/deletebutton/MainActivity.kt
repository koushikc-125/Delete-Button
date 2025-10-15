package com.example.deletebutton

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.example.deletebutton.designsystem.components.ThemePreview
import com.example.deletebutton.designsystem.theme.DeleteButtonTheme
import com.example.deletebutton.ui.ScreenRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}

@ThemePreview
@Composable
private fun App() {
    DeleteButtonTheme {
        Scaffold { paddingValues ->
            ScreenRoot(paddingValues)
        }
    }
}
