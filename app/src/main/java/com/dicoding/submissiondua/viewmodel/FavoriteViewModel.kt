package com.dicoding.submissiondua.viewmodel

import android.app.Application
import android.database.Cursor
import androidx.lifecycle.AndroidViewModel
import com.dicoding.submissiondua.local.FavoriteUser
import com.dicoding.submissiondua.model.User
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private var favUser = FavoriteUser(application)
    private var favoriteUsername: Cursor? = null

    fun getFavorites(): Cursor? {
        return favUser.getFavorites()
    }

    fun getFavoriteByUsername(username: String): Cursor? {
        favoriteUsername = favUser.getFavoriteByUsername(username)
        return favoriteUsername
    }

    fun insertFavorite(user: User) {
        favUser.insert(user)
    }

    fun deleteFavorite(username: String) {
        favUser.delete(username)
    }
}

