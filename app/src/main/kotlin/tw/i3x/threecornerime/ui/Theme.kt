package tw.i3x.threecornerime.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1976D2),
    onPrimary = Color.White,
    surface = Color(0xFFECEFF1),
    onSurface = Color(0xFF212121),
    surfaceVariant = Color(0xFFFFFFFF),
    onSurfaceVariant = Color(0xFF424242),
    outline = Color(0xFFBDBDBD),
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF5B9BD5),
    onPrimary = Color.White,
    surface = Color(0xFF2C2C2C),
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = Color(0xFF3C3C3C),
    onSurfaceVariant = Color(0xFFCCCCCC),
    outline = Color(0xFF555555),
)

@Composable
fun ThreeCornerTheme(content: @Composable () -> Unit) {
    val colorScheme = if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
