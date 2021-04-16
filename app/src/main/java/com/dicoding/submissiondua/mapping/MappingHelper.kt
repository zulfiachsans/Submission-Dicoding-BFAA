package com.dicoding.submissiondua.mapping

import android.database.Cursor
import com.dicoding.submissiondua.model.User

object MappingHelper {
    fun mapCursorToArrayList(favoriteCursor: Cursor?): ArrayList<User> {
        val favList = ArrayList<User>()

        favoriteCursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow("username"))
                val avatar = getString(getColumnIndexOrThrow("avatar"))
                val tipe = getString(getColumnIndexOrThrow("type"))
                val detail_url = getString(getColumnIndexOrThrow("detail_url"))
                val followers_url = getString(getColumnIndexOrThrow("followers_url"))
                val following_url = getString(getColumnIndexOrThrow("following_url"))
                favList.add(User(username, avatar, tipe, detail_url, followers_url, following_url))
            }
        }
        return favList
    }

    fun Cursor.mapCursorToObject(): User? {
        this.apply {
            return if (moveToFirst()) {
                val username = getString(getColumnIndexOrThrow("username"))
                val avatar = getString(getColumnIndexOrThrow("avatar"))
                val tipe = getString(getColumnIndexOrThrow("type"))
                val detail_url = getString(getColumnIndexOrThrow("detail_url"))
                val followers_url = getString(getColumnIndexOrThrow("followers_url"))
                val following_url = getString(getColumnIndexOrThrow("following_url"))
                User(username, avatar, tipe, detail_url, followers_url, following_url)
            } else {
                null
            }
        }
    }
}