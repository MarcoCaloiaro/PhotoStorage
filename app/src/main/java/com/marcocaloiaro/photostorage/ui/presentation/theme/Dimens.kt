package com.marcocaloiaro.photostorage.ui.presentation.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimens(
    val padding: Dp,
    val padding_half: Dp,
    val padding_double: Dp,
    val radius_shape: Dp
)

val appDimensions = Dimens(
    padding_half = 8.dp,
    padding = 16.dp,
    padding_double = 32.dp,
    radius_shape = 12.dp
)