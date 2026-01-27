package org.example.project.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

import org.jetbrains.compose.resources.Font
import spadebreaklive.composeapp.generated.resources.*

val Poppins: FontFamily
    @Composable get() = FontFamily(
        Font(resource = Res.font.poppins_bold, weight = FontWeight.Bold),
        Font(resource = Res.font.poppins_semibold, weight = FontWeight.SemiBold),
        Font(resource = Res.font.poppins_medium, weight = FontWeight.Medium),
        Font(resource = Res.font.poppins_regular, weight = FontWeight.Normal)
    )





val VendorTypography: Typography
    @Composable get() = Typography(
        displayLarge = TextStyle(  // BIGGEST HEADLINE
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
        ),
        headlineLarge = TextStyle( // Large heading
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
        ),
        headlineMedium = TextStyle( // Medium heading
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp,
        ),
        headlineSmall = TextStyle( // Small heading
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
        ),
        titleLarge = TextStyle( // Page title
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 22.sp,
        ),
        titleMedium = TextStyle( // Section title
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
        ),
        titleSmall = TextStyle( // Sub section
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
        ),
        bodyLarge = TextStyle( // Primary body text
            fontFamily = Poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        ),
        bodyMedium = TextStyle( // Secondary body text
            fontFamily = Poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
        ),
        bodySmall = TextStyle( // Small details
            fontFamily = Poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
        ),
        labelLarge = TextStyle( // Buttons
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
        ),
        labelMedium = TextStyle( // Chips, tabs
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
        ),
        labelSmall = TextStyle( // Small badges
            fontFamily = Poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 10.sp,
        ),
    )
