package org.example.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import org.example.project.features.entry.presentation.HomeScreen
import org.example.project.navigation.NavGraph
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(){
    MaterialTheme {
       NavGraph()
    }
}