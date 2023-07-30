package com.marcocaloiaro.photostorage.ui.main

import com.marcocaloiaro.photostorage.data.LocalPhoto

data class MainViewState(
    val photos: List<LocalPhoto>,
    val shouldLoad: Boolean
) {
    companion object {
        val empty = MainViewState(
            photos = emptyList(),
            shouldLoad = true
        )
    }
}