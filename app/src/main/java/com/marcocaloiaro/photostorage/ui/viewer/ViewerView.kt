package com.marcocaloiaro.photostorage.ui.viewer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.marcocaloiaro.photostorage.ui.presentation.theme.PhotoStorageTheme
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun ViewerView(
    triggerIntent: (ViewerIntent) -> Unit,
    selectedPhotoId: Int,
    viewState: ViewerViewState,
    effects: SharedFlow<ViewerEffect>,
    onScreenClosed: () -> Unit
) {

    LaunchedEffect(key1 = selectedPhotoId) {
        triggerIntent(ViewerIntent.ScreenLaunched(selectedPhotoId))
        effects.collect { effect ->
            when (effect) {
                ViewerEffect.CloseScreen -> onScreenClosed()
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        IconButton(
            onClick = { triggerIntent(ViewerIntent.CloseClicked) }
        ) {
            Icon(
                Icons.Filled.Close,
                contentDescription = null
            )
        }
        if (viewState.shouldLoad) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            AsyncImage(
                model = viewState.photoPath,
                contentDescription = null,
                contentScale = ContentScale.None,
                modifier = Modifier
                    .padding(
                        vertical = PhotoStorageTheme.dimens.padding_double,
                    )
                    .padding(
                        horizontal = PhotoStorageTheme.dimens.padding
                    )
                    .fillMaxSize()
            )
        }
    }
}