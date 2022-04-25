package com.example.myaudiobookplayer.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myaudiobookplayer.database.dao.BookDao
import kotlin.jvm.Volatile
import com.example.myaudiobookplayer.database.entity.Book

@Database(entities = [Book::class], exportSchema = false, version = 1)
abstract class BookRoomDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao?

    companion object {
        @Volatile
        private var INSTANCE: BookRoomDatabase? = null


        fun getDatabase(context: Context): BookRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(BookRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            BookRoomDatabase::class.java,
                            "book.db").build()
                    }
                }
            }
            return INSTANCE
        }
    }
}