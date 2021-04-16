package com.dicoding.consumerapp.model

import android.content.ContentValues
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "user_favorite")
data class User(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "username")
    var username: String,
    @ColumnInfo(name = "avatar")
    var avatar: String?,
    @ColumnInfo(name = "type")
    var tipe: String?,
    @ColumnInfo(name = "detail_url")
    var detail_url: String?,
    @ColumnInfo(name = "followers_url")
    var followers_url: String?,
    @ColumnInfo(name = "following_url")
    var following_url: String?,
) : Parcelable

fun fromContentValues(values: ContentValues?): User {
    return User(
        values?.getAsString("username") ?: "null",
        values?.getAsString("type")?: "nul",
        values?.getAsString("avatar") ?: "null",
        values?.getAsString("detail_url") ?: "null",
        values?.getAsString("followers_url") ?: "null",
        values?.getAsString("following_url") ?: "null"
    )
}