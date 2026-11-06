package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
  primary = Color(0xFF93C5FD),
  onPrimary = RoyalBlue900,
  primaryContainer = RoyalBlue800,
  onPrimaryContainer = RoyalBlue100,
  secondary = OrangeAccent400,
  onSecondary = Color.Black,
  secondaryContainer = Color(0xFF7C2D12),
  onSecondaryContainer = OrangeAccent100,
  tertiary = OrangeAccent,
  onTertiary = Color.White,
  background = BackgroundDark,
  onBackground = TextPrimaryDark,
  surface = SurfaceDark,
  onSurface = TextPrimaryDark,
  surfaceVariant = SurfaceVariantDark,
  onSurfaceVariant = TextSecondaryDark,
  outline = BorderDark,
  error = Color(0xFFEF4444)
)

private val LightColorScheme = lightColorScheme(
  primary = RoyalBlue800,
  onPrimary = Color.White,
  primaryContainer = RoyalBlue100,
  onPrimaryContainer = RoyalBlue900,
  secondary = OrangeAccent,
  onSecondary = Color.White,
  secondaryContainer = OrangeAccent100,
  onSecondaryContainer = OrangeAccent700,
  tertiary = RoyalBlue600,
  onTertiary = Color.White,
  background = BackgroundLight,
  onBackground = TextPrimaryLight,
  surface = SurfaceLight,
  onSurface = TextPrimaryLight,
  surfaceVariant = SurfaceVariantLight,
  onSurfaceVariant = TextSecondaryLight,
  outline = BorderLight,
  error = Color(0xFFDC2626)
)

@Composable
fun ChamparanTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep brand colors distinct and consistent
  content: @Composable () -> Unit
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
