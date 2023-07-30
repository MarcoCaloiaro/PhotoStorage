package com.marcocaloiaro.photostorage.ui.viewer

sealed class ViewerIntent {
    data class ScreenLaunched(val photoId: Int): ViewerIntent()
    object CloseClicked : ViewerIntent()
}