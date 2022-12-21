package com.hisu.movieapp.ui.home.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.hisu.movieapp.app.MyApplication
import com.hisu.movieapp.data.repository.MovieRepositoryService
import com.hisu.movieapp.model.MoviesResponse
import com.hisu.movieapp.utils.Resource
import com.hisu.movieapp.utils.Utils
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class MovieHomeViewModel(
    val app: Application,
    private val movieRepository: MovieRepositoryService
) : AndroidViewModel(app) {

    private val _popularMovies = MutableLiveData<Resource<MoviesResponse>>()
    val popularMovie: LiveData<Resource<MoviesResponse>>
        get() = _popularMovies

    private val _trendingMovies = MutableLiveData<Resource<MoviesResponse>>()
    val trendingMovies: LiveData<Resource<MoviesResponse>>
        get() = _trendingMovies

    fun getPopularMovies(queries: Map<String, String>) = viewModelScope.launch {
        fetchPopularMovies(queries)
    }

    fun getTrendingMovies() = viewModelScope.launch {
        fetchTrendingMovies()
    }

    private suspend fun fetchTrendingMovies() {
        _trendingMovies.postValue(Resource.Loading())

        try {
            if (Utils.hasInternetConnection(getApplication<MyApplication>())) {
                val response = movieRepository.getTrendingMovies()
                _trendingMovies.postValue(handleMoviesResponse(response))
            } else {
                _trendingMovies.postValue(Resource.Error("Network Error"))
            }
        } catch (t: Exception) {
            when (t) {
                is IOException -> _trendingMovies.postValue(Resource.Error(t.localizedMessage!!))
                else -> _trendingMovies.postValue(Resource.Error(t.localizedMessage!!))
            }
        }
    }

    private suspend fun fetchPopularMovies(queries: Map<String, String>) {
        _popularMovies.postValue(Resource.Loading())

        try {
            if (Utils.hasInternetConnection(getApplication<MyApplication>())) {
                val response = movieRepository.getPopularMovies(queries)
                _popularMovies.postValue(handleMoviesResponse(response))
            } else {
                _popularMovies.postValue(Resource.Error("Network Error"))
            }
        } catch (t: Exception) {
            when (t) {
                is IOException -> _popularMovies.postValue(Resource.Error(t.message!!))
                else -> _popularMovies.postValue(Resource.Error(t.message!!))
            }
        }
    }

    private fun handleMoviesResponse(response: Response<Any>): Resource<MoviesResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                val gson = Gson()
                val json = gson.toJsonTree(it)
                val result = gson.fromJson(
                    json.asJsonObject.get("results"),
                    MoviesResponse::class.java
                )

                return Resource.Success(result)
            }
        }

        return Resource.Error(response.message())
    }
}