package com.marcocaloiaro.photostorage.ui.viewer

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
class ViewerViewModel @Inject constructor(
    private val photoFetchManager: PhotoFetchManager
) : ViewModel() {

    val viewState = mutableStateOf(ViewerViewState.empty)

    private val effects: MutableSharedFlow<ViewerEffect> = MutableSharedFlow()

    fun processEffects(): SharedFlow<ViewerEffect> {
        return effects.asSharedFlow()
    }

    fun onIntentTriggered(intent: ViewerIntent) {
        when (intent) {
            is ViewerIntent.ScreenLaunched -> getPhotoPath(intent.photoId)
            ViewerIntent.CloseClicked -> closeScreen()
        }
    }

    private fun closeScreen() {
        viewModelScope.launch {
            effects.emit(ViewerEffect.CloseScreen)
        }
    }

    private fun getPhotoPath(photoId: Int) {
        viewModelScope.launch {
            val photoPath = photoFetchManager.getPhotoPath(photoId)
            viewState.value = viewState.value.copy(photoPath = photoPath, shouldLoad = false)
        }
    }
}