package com.dicoding.consumerapp.local

import android.app.Application
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.dicoding.consumerapp.local.DatabaseContract.UserColumns.Companion.CONTENT_URI
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class FavoriteUser(private val application: Application) {

    private lateinit var uriWithUsername: Uri

    private var favorites: Cursor? = null
    private var favoriteUsername: Cursor? = null

    fun getFavorites(): Cursor? {
        favorites = application.contentResolver.query(CONTENT_URI, null, null, null, null)
        return favorites
    }

    fun getFavoriteByUsername(username: String): Cursor? {
        uriWithUsername = Uri.parse("$CONTENT_URI/$username")
        favoriteUsername =
            application.contentResolver.query(uriWithUsername, null, null, null, null)
        return favoriteUsername
    }

    fun insert(values: ContentValues) {
        application.contentResolver.insert(CONTENT_URI, values)
    }

    fun delete(username: String) {
        uriWithUsername = Uri.parse("$CONTENT_URI/$username")
        application.contentResolver.delete(uriWithUsername, null, null)
    }
}