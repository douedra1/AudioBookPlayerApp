package com.example.myaudiobookplayer.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myaudiobookplayer.database.entity.Book
import java.util.*

@Dao
interface BookDao {

    @Insert
    fun insertBook(book: Book?): Long

    @Query("Select * from BookDetails")
    fun getAllBooks(): LiveData<List<Book>>?

    @Update
    fun updateBook(book: Book)

    @Query("Select * from BookDetails WHERE id = :id order by id DESC ")
    fun getBook(id: Int): Book

    @Query("DELETE FROM BookDetails WHERE id = :id")
    fun deleteBook(id: Int)

}