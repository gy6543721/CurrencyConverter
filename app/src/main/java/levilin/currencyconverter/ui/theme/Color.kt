package levilin.currencyconverter.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val LightBlue = Color(0xFFB6DFF3)
val Blue = Color(0xFF32A5DD)
val DarkBlue = Color(0xFF058ACA)

val LightGray = Color(0xFFFCFCFC)
val MediumGray = Color(0xFF9C9C9C)
val DarkGray = Color(0xFF5A5959)


val Colors.converterScreenBackgroundColor: Color
    @Composable
    get() = if (isLight) LightGray else DarkGray

val Colors.converterScreenTextColor: Color
    @Composable
    get() = if (isLight) DarkGray else LightGray

val Colors.boxBorderColor: Color
    @Composable
    get() = if (isLight) MediumGray else MediumGray

val Colors.boxBackgroundColor: Color
    @Composable
    get() = if (isLight) Blue else LightBlue

val Colors.boxIconColor: Color
    @Composable
    get() = if (isLight) MediumGray else MediumGray