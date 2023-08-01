package com.marcocaloiaro.photostorage.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.marcocaloiaro.photostorage.ui.presentation.navigation.Navigation
import com.marcocaloiaro.photostorage.ui.presentation.theme.PhotoStorageTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhotoStorageTheme {
                Navigation()
            }
        }
    }
}
