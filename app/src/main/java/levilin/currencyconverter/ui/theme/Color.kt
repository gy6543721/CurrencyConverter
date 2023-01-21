package levilin.currencyconverter.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val LightBlue = Color(0xFFC1E9FD)
val Blue = Color(0xFF32A5DD)
val DarkBlue = Color(0xFF033D5A)

val LightGray = Color(0xFFFAFAFA)
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
    get() = if (isLight) LightBlue else Blue

val Colors.boxIconColor: Color
    @Composable
    get() = if (isLight) MediumGray else DarkGray

val Colors.contentTextColor: Color
    @Composable
    get() = if (isLight) DarkBlue else LightGray