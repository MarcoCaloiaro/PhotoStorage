package com.marcocaloiaro.photostorage.ui.main

sealed class MainIntent {
    object ScreenLaunched : MainIntent()
    data class PhotoSelected(val photoId: Int): MainIntent()
}