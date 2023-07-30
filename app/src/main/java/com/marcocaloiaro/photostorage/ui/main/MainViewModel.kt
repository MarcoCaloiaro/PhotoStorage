package com.marcocaloiaro.photostorage.ui.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcocaloiaro.photostorage.data.local.PhotoFetchManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val photoFetchManager: PhotoFetchManager
) : ViewModel() {

    val viewState = mutableStateOf(MainViewState.empty)

    private val effects: MutableSharedFlow<MainEffect> = MutableSharedFlow()

    fun onIntentTriggered(intent: MainIntent) {
        when (intent) {
            MainIntent.ScreenLaunched -> getAllPhotos()
            is MainIntent.PhotoSelected -> selectPhoto(intent.photoId)
        }
    }

    fun processEffects(): SharedFlow<MainEffect> {
        return effects.asSharedFlow()
    }

    private fun selectPhoto(photoId: Int) {
        viewModelScope.launch {
            effects.emit(MainEffect.OpenPhoto(photoId))
        }
    }

    private fun getAllPhotos() {
        viewModelScope.launch {
            val photos = photoFetchManager.getAllPhotos()
            viewState.value = viewState.value.copy(photos = photos, shouldLoad = false)
        }
    }

}

