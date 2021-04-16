package com.dicoding.submissiondua.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.submissiondua.model.User
import kotlinx.coroutines.InternalCoroutinesApi


@Database(
    entities = [User::class],
    exportSchema = false, version = 1
)
abstract class DatabaseUser : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteUserDao

    companion object {

        private const val DB_NAME = "user_favorite"
        private var INSTANCE: DatabaseUser? = null

        @InternalCoroutinesApi
        fun getInstance(context: Context): DatabaseUser {
            return INSTANCE ?: kotlinx.coroutines.internal.synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseUser::class.java,
                    DB_NAME
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}