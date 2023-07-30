package com.marcocaloiaro.photostorage.ui.presentation.navigation

sealed class Screen(val route: String) {
    object Feed : Screen("feedScreen")
    object ImageViewer : Screen("imageViewerScreen/")
}