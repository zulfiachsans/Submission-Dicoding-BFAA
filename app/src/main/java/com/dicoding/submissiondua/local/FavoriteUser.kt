package com.dicoding.submissiondua.local

import android.app.Application
import android.database.Cursor
import com.dicoding.submissiondua.model.User
import kotlinx.coroutines.InternalCoroutinesApi



@InternalCoroutinesApi
class FavoriteUser(private val application: Application) {

    private val dao: FavoriteUserDao?
    private var favorites: Cursor? = null
    private var favoriteUsername: Cursor? = null

    init {
        val db = DatabaseUser.getInstance(application.applicationContext)
        dao = db.favoriteDao()
    }

    fun getFavorites(): Cursor? {
        favorites = dao?.getFavorites()
        return favorites
    }

    fun getFavoriteByUsername(username: String): Cursor? {
        favoriteUsername = dao?.getFavoriteByUsername(username)
        return favoriteUsername
    }

    fun insert(user: User) {
        dao?.insertFavorite(user)
    }

    fun delete(username: String) {
        dao?.deleteFavorite(username)
    }
}