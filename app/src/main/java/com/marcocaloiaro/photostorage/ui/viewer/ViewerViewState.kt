package com.marcocaloiaro.photostorage.ui.viewer

data class ViewerViewState(
    val photoPath: String?,
    val shouldLoad: Boolean
) {
    companion object {
        val empty = ViewerViewState(
            photoPath = null,
            shouldLoad = true
        )
    }
}