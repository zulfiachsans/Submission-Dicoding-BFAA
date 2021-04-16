package com.dicoding.submissiondua.local

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.submissiondua.model.User

@Dao
interface FavoriteUserDao {
    @Query("SELECT * FROM user_favorite")
    fun getFavorites(): Cursor?

    @Query("SELECT * FROM user_favorite where username = :username")
    fun getFavoriteByUsername(username: String): Cursor?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(user: User): Long

    @Query("DELETE FROM user_favorite WHERE username = :username")
    fun deleteFavorite(username: String): Int
}