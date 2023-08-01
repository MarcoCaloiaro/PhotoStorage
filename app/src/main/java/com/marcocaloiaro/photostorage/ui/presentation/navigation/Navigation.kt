package com.marcocaloiaro.photostorage.ui.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.marcocaloiaro.photostorage.ui.main.MainView
import com.marcocaloiaro.photostorage.ui.main.MainViewModel
import com.marcocaloiaro.photostorage.ui.viewer.ViewerView
import com.marcocaloiaro.photostorage.ui.viewer.ViewerViewModel

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Feed.route) {
        composable(
            route = Screen.Feed.route
        ) {
            val viewModel: MainViewModel = hiltViewModel()
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
            route = Screen.ImageViewer.route + "{photoId}",
            arguments = listOf(navArgument("photoId") { type = NavType.IntType }),
        ) { entry ->
            val photoId = entry.arguments?.getInt("photoId") ?: 0
            val viewModel: ViewerViewModel = hiltViewModel()
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