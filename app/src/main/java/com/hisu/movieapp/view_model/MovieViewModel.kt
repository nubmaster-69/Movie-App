package com.hisu.movieapp.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.hisu.movieapp.data.repository.MovieRepository
import com.hisu.movieapp.model.MovieDetail
import com.hisu.movieapp.model.MoviePreviewResult

class MovieViewModel : ViewModel() {

    private val _movieID: MutableLiveData<String> = MutableLiveData()

    fun getPopularMovies(queries: Map<String, String>): LiveData<List<MoviePreviewResult>> {
        return MovieRepository.getPopularMovies(queries)
    }

    fun setMovieID(movieID: String) {
        if (_movieID.value == movieID)
            return

        _movieID.value = movieID
    }

    fun getMovieDetail(movieID: String, queries: Map<String, String>): LiveData<MovieDetail> {
        return Transformations.switchMap(_movieID) {
            MovieRepository.getMovieDetail(it, queries)
        }
    }

    fun cancelJob() {
        MovieRepository.cancelJob()
    }
}