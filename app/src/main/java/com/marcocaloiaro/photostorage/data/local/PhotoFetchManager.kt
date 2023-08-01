package com.marcocaloiaro.photostorage.data.local

import android.content.Context
import android.provider.MediaStore
import com.marcocaloiaro.photostorage.data.LocalPhoto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class PhotoFetchManager @Inject constructor(
    @ApplicationContext val context: Context
) {

    suspend fun getAllPhotos(): List<LocalPhoto> {
        val imagesList = mutableListOf<LocalPhoto>()
        val imagesCursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA),
            null,
            null,
            MediaStore.Images.Media.DATE_ADDED + " DESC"
        )

        if (imagesCursor != null) {
            return withContext(Dispatchers.IO) {
                while (imagesCursor.moveToNext()) {
                    val id =
                        imagesCursor.getLong(
                            imagesCursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                        )
                    val photoUri =
                        imagesCursor.getString(
                            imagesCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                        )
                    photoUri?.let { imagesList.add(LocalPhoto(photoUri, id.toInt())) }
                }
                imagesCursor?.close()
                return@withContext imagesList
            }
        } else {
            return imagesList
        }
    }

    suspend fun getPhotoPath(photoId: Int): String? {

        val imageCursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Images.Media.DATA),
            MediaStore.Images.Media._ID + "=?",
            arrayOf(photoId.toString()),
            null
        )

        return withContext(Dispatchers.IO) {
            if (imageCursor != null && imageCursor.moveToFirst()) {
                val photoUri = imageCursor.getString(
                    imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                )
                imageCursor.close()
                return@withContext photoUri.toString()
            }
            return@withContext null
        }
    }

}