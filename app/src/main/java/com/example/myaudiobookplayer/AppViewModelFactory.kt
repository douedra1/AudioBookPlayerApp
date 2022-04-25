package com.example.myaudiobookplayer

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myaudiobookplayer.database.repository.BookRepository
import java.lang.reflect.InvocationTargetException


class AppViewModelFactory(
    val app: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppViewModel(app, BookRepository(app)) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}