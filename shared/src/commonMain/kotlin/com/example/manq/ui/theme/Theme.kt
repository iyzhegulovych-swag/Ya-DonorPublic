package com.example.manq.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Темно-зелений — основний колір додатку, кораловий — акцент для дій.
// Кремове тло замість чисто білого: менше втомлює очі на довгих списках.
private val Teal = Color(0xFF0F6B5A)
private val TealDark = Color(0xFF0A4F42)
private val TealContainer = Color(0xFFDCEDE7)
private val Coral = Color(0xFFF26B4E)
private val Cream = Color(0xFFFDF7F0)
private val Ink = Color(0xFF123A33)

private val LightColors = lightColorScheme(
    primary = Teal,
    onPrimary = Color.White,
    primaryContainer = TealContainer,
    onPrimaryContainer = TealDark,
    secondary = Coral,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFE2D9),
    onSecondaryContainer = Color(0xFF7A2A16),
    background = Cream,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    surfaceVariant = Color(0xFFF1EDE6),
    onSurfaceVariant = Color(0xFF55615D),
    outline = Color(0xFFBFC7C4),
    error = Color(0xFFC0392B),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF6FD3BC),
    onPrimary = Color(0xFF00382E),
    primaryContainer = Color(0xFF13453B),
    onPrimaryContainer = Color(0xFFBDEFE2),
    secondary = Color(0xFFFF9B7F),
    onSecondary = Color(0xFF5A1B0A),
    secondaryContainer = Color(0xFF6D2B18),
    onSecondaryContainer = Color(0xFFFFDBD0),
    background = Color(0xFF121614),
    onBackground = Color(0xFFE3E6E3),
    surface = Color(0xFF1B211F),
    onSurface = Color(0xFFE3E6E3),
    surfaceVariant = Color(0xFF2A302E),
    onSurfaceVariant = Color(0xFFB3BDB9),
    outline = Color(0xFF4A5450),
    error = Color(0xFFFF8A80),
)

/** Рівень потреби у крові. Кольори спеціально не з палітри теми. */
object NeedColors {
    val high = Color(0xFFD64545)
    val medium = Color(0xFFE8A33D)
    val low = Color(0xFF2E9E6B)
}

@Composable
fun YaDonorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
