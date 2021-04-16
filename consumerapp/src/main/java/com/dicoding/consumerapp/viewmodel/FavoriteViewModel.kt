package com.dicoding.consumerapp.viewmodel

import android.app.Application
import android.content.ContentValues
import android.database.Cursor
import androidx.lifecycle.AndroidViewModel
import com.dicoding.consumerapp.local.FavoriteUser
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private var repository = FavoriteUser(application)
    private var favoriteUsername: Cursor? = null

    fun getFavorites(): Cursor? {
        return repository.getFavorites()
    }

    fun getFavoriteByUsername(username: String): Cursor? {
        favoriteUsername = repository.getFavoriteByUsername(username)
        return favoriteUsername
    }

    fun insertFavorite(values: ContentValues) {
        repository.insert(values)
    }

    fun deleteFavorite(username: String) {
        repository.delete(username)
    }
}
