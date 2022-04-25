package com.example.myaudiobookplayer.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "BookDetails")
class Book(
    @PrimaryKey(autoGenerate = true) var bookId: Long = 0,
    @ColumnInfo val id: Int,
    @ColumnInfo val duration: Int,
    @ColumnInfo var currentPosition: Int = 0,
    @ColumnInfo var localPath: String? = "",
    @ColumnInfo val title: String,
    @ColumnInfo val author: String,
    @ColumnInfo val cover_url: String
)