package com.dicoding.consumerapp.local

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.dicoding.submissiondua"
    const val SCHEME = "content"

    class UserColumns : BaseColumns {

        companion object {
            const val TABLE_NAME = "user_favorite"
            const val username = "username"
            const val avatar = "avatar"
            const val tipe = "type"
            const val detail_url = "detail_url"
            const val followers_url = "followers_url"
            const val following_url = "following_url"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }

    }
}