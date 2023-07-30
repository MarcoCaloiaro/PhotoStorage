package com.marcocaloiaro.photostorage.ui.presentation.composebase

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import com.marcocaloiaro.photostorage.R
import com.marcocaloiaro.photostorage.ui.presentation.theme.PhotoStorageTheme

@ExperimentalPermissionsApi
@Composable
fun PermissionHandler(
    permissionState: PermissionState,
    rationale: String,
    onSettingsClick: () -> Unit,
    permissionNotAvailableContent: @Composable () -> Unit = {
        PermissionNotAvailable(
            onClick = onSettingsClick
        )
    },
    content: @Composable () -> Unit = {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        )
    },
) {
    LaunchedEffect(Unit) {
        if (!permissionState.status.isGranted && !permissionState.status.shouldShowRationale) {
            permissionState.launchPermissionRequest()
        }
    }
    when (permissionState.status) {
        PermissionStatus.Granted -> content()
        is PermissionStatus.Denied -> {
            if (permissionState.status.shouldShowRationale) {
                Rationale(
                    text = rationale,
                    onRequestPermission = {
                        permissionState.launchPermissionRequest()
                    }
                )
            } else {
                permissionNotAvailableContent()
            }
        }
    }
}

@Composable
private fun Rationale(
    text: String,
    onRequestPermission: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                    text = stringResource(id = R.string.permission_request),
                    modifier = Modifier
                        .padding(horizontal = PhotoStorageTheme.dimens.padding)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h4
                )
            },
            text = {
                Text(
                    text = text,
                    modifier = Modifier
                        .padding(horizontal = PhotoStorageTheme.dimens.padding)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body2
                )
            },
            confirmButton = {
                Button(
                    onClick = onRequestPermission,
                    modifier = Modifier.padding(bottom = PhotoStorageTheme.dimens.padding)
                ) {
                    Text(text = stringResource(id = R.string.grant_permission_action))
                }
            },
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
private fun PermissionNotAvailable(
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.permissions_missing_alert),
            modifier = Modifier
                .padding(horizontal = PhotoStorageTheme.dimens.padding)
                .fillMaxWidth(),
            color = Color.White
        )
        Button(
            onClick = onClick,
            modifier = Modifier.padding(top = PhotoStorageTheme.dimens.padding)
        ) {
            Text(text = stringResource(id = R.string.permission_settings_action))
        }
    }
}

fun Context.openSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    this.startActivity(intent)
}