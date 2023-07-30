package com.marcocaloiaro.photostorage.ui.main

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.marcocaloiaro.photostorage.R
import com.marcocaloiaro.photostorage.ui.presentation.composebase.PermissionHandler
import com.marcocaloiaro.photostorage.ui.presentation.composebase.openSettings
import com.marcocaloiaro.photostorage.ui.presentation.theme.PhotoStorageTheme
import kotlinx.coroutines.flow.SharedFlow

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainView(
    triggerIntent: (MainIntent) -> Unit,
    viewState: MainViewState,
    effects: SharedFlow<MainEffect>,
    onPhotoSelected: (Int) -> Unit
) {

    val lazyGridState = rememberLazyGridState()

    val permissionState = rememberPermissionState(
        if (Build.VERSION.SDK_INT > 32) {
            "android.permission.READ_MEDIA_IMAGES"
        } else {
            "android.permission.READ_EXTERNAL_STORAGE"
        }
    )

    LaunchedEffect(key1 = permissionState.status) {
        triggerIntent(MainIntent.ScreenLaunched)
        effects.collect { effect ->
            when (effect) {
                is MainEffect.OpenPhoto -> onPhotoSelected(effect.photoId)
            }
        }
    }

    val context = LocalContext.current

    PermissionHandler(
        permissionState = permissionState,
        rationale = stringResource(id = R.string.storage_permission_rationale_text),
        onSettingsClick = { context.openSettings() }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (viewState.shouldLoad) {
                CircularProgressIndicator(
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .padding(bottom = PhotoStorageTheme.dimens.padding_half)
                        .fillMaxSize(),
                    state = lazyGridState
                ) {
                    items(viewState.photos) { photo ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(
                                    top = PhotoStorageTheme.dimens.padding
                                )
                                .padding(
                                    horizontal = PhotoStorageTheme.dimens.padding
                                )
                                .clickable {
                                    triggerIntent(MainIntent.PhotoSelected(photo.id))
                                },
                            elevation = 8.dp,
                            shape = RoundedCornerShape(PhotoStorageTheme.dimens.radius_shape),
                        ) {
                            AsyncImage(
                                model = photo.path,
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }

        }
    }
}