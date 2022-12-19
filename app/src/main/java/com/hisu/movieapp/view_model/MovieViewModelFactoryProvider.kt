package com.hisu.movieapp.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hisu.movieapp.data.repository.MovieRepositoryService

class MovieViewModelFactoryProvider(
    val app: Application,
    val movieRepository: MovieRepositoryService
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieHomeViewModel::class.java))
            return MovieHomeViewModel(app, movieRepository) as T

        throw IllegalArgumentException("Unknown class name")
    }
}