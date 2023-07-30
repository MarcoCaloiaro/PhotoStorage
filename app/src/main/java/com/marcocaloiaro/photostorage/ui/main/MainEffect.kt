package com.marcocaloiaro.photostorage.ui.main

sealed class MainEffect {
    data class OpenPhoto(val photoId: Int): MainEffect()
}