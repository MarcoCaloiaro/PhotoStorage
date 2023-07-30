package com.marcocaloiaro.photostorage.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.marcocaloiaro.photostorage.ui.presentation.navigation.Screen
import com.marcocaloiaro.photostorage.ui.presentation.theme.PhotoStorageTheme
import com.marcocaloiaro.photostorage.ui.viewer.ViewerView
import com.marcocaloiaro.photostorage.ui.viewer.ViewerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhotoStorageTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.Feed.route) {
                    composable(
                        route = Screen.Feed.route
                    ) {
                        val viewModel: MainViewModel by viewModels()
                        MainView(
                            triggerIntent = viewModel::onIntentTriggered,
                            viewState = viewModel.viewState.value,
                            effects = viewModel.processEffects(),
                            onPhotoSelected = { selectedPhotoId ->
                                navController.navigate(
                                    Screen.ImageViewer.route + selectedPhotoId
                                )
                            },
                        )
                    }
                    composable(
                        route = Screen.ImageViewer.route + "{$PHOTO_ID}",
                        arguments = listOf(navArgument(PHOTO_ID) { type = NavType.IntType }),
                    ) { entry ->
                        val photoId = entry.arguments?.getInt(PHOTO_ID) ?: 0
                        val viewModel: ViewerViewModel by viewModels()
                        ViewerView(
                            triggerIntent = viewModel::onIntentTriggered,
                            effects = viewModel.processEffects(),
                            viewState = viewModel.viewState.value,
                            selectedPhotoId = photoId,
                            onScreenClosed = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }

    companion object {
        private const val PHOTO_ID = "photoId"
    }
}
