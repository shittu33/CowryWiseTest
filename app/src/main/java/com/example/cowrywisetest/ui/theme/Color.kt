package com.example.cowrywisetest.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)


val LightWhiteColor = Color(0xFFFFFFFF)
val LightGreyColor = Color(0xFFCCC9C9)
val LightGreyColor2 = Color(0xFFDEE2E7)
val LightGreyColor3 = Color(0xFFC9CBCC)
val LightSuccessButtonColor1 = Color(0xFF11D79B)
val LightHintColor = Color(0xFFA5A8A8)


@Immutable
data class CustomColorsPalette(
    val grey: Color = Color.Unspecified,
    val grey2: Color = Color.Unspecified,
    val onSurface: Color = Color.Unspecified,
    val accentColor: Color = Color.Unspecified,
    val bgColor: Color = Color.Unspecified,
    val successButtonColor1: Color = Color.Unspecified,
    val textInputContainerColor: Color = Color.Unspecified,
    val hintColor: Color = Color.Unspecified,
)

val LightCustomColorsPalette = CustomColorsPalette(
    grey = LightGreyColor,
    grey2 = LightGreyColor2,
    accentColor = Color(0xFF2C72DE),
    onSurface = Color.Black.copy(alpha = 0.7f),
    bgColor = LightWhiteColor,
    successButtonColor1 = LightSuccessButtonColor1,
    textInputContainerColor = LightGreyColor3.copy(0.15f),
    hintColor = LightHintColor
)

val DarkCustomColorsPalette = CustomColorsPalette(
    grey = LightGreyColor,
    grey2 = LightGreyColor2,
    onSurface = Color.White,
    accentColor = Color(0xFF2C72DE),
    bgColor = Color.Black,
    successButtonColor1 = LightSuccessButtonColor1,
    textInputContainerColor = LightGreyColor3.copy(0.15f),
    hintColor = LightHintColor
)

val LocalColors = staticCompositionLocalOf { CustomColorsPalette() }
val CowryWiseCustomColorsPalette: CustomColorsPalette
    @Composable
    get() = LocalColors.current