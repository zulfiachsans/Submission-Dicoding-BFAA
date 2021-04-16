package com.dicoding.submissiondua.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.dicoding.submissiondua.local.DatabaseUser
import com.dicoding.submissiondua.local.FavoriteUserDao
import com.dicoding.submissiondua.model.fromContentValues
import kotlinx.coroutines.InternalCoroutinesApi

class ContentUserProvider: ContentProvider() {
    companion object {
        private const val AUTHORITY = "com.dicoding.submissiondua"
        private const val SCHEME = "content"
        private const val TABLE_NAME = "user_favorite"

        val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build()

        private const val USER = 1
        private const val USERNAME = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favoriteUserDao: FavoriteUserDao
        private lateinit var databaseUser: DatabaseUser

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, USER)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/*", USERNAME)
        }

    }

    @InternalCoroutinesApi
    override fun onCreate(): Boolean {
        databaseUser = DatabaseUser.getInstance(context as Context)
        favoriteUserDao = databaseUser.favoriteDao()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?,
    ): Cursor? {
        return when (sUriMatcher.match(uri)) {
            USER -> favoriteUserDao.getFavorites()
            USERNAME -> favoriteUserDao.getFavoriteByUsername(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (USER) {
            sUriMatcher.match(uri) -> favoriteUserDao.insertFavorite(fromContentValues(values))
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?,
    ): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (USERNAME) {
            sUriMatcher.match(uri) -> favoriteUserDao.deleteFavorite(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }
}