package org.example.project.presentation.ui.component

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.example.project.presentation.ui.theme.darkPrimaryBlue

@Composable
actual fun loadingProgressBar(modifier: Modifier) {
    CircularProgressIndicator(
        modifier = modifier,
        color = androidx.compose.material3.MaterialTheme.colorScheme.darkPrimaryBlue,
    )
}