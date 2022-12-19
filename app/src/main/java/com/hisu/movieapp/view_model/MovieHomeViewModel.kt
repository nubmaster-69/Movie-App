package com.hisu.movieapp.view_model

import android.app.Application
import androidx.lifecycle.*
import com.google.gson.Gson
import com.hisu.movieapp.app.MyApplication
import com.hisu.movieapp.data.repository.MovieRepositoryService
import com.hisu.movieapp.model.MovieDetail
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

    private val _movieDetail = MutableLiveData<Resource<MovieDetail>>()
    val movieDetail: LiveData<Resource<MovieDetail>>
        get() = _movieDetail

    fun getMovieDetail(movieID: String, queries: Map<String, String>) = viewModelScope.launch {
        fetchMovieDetail(movieID, queries)
    }

    fun getPopularMovies(queries: Map<String, String>) = viewModelScope.launch {
        fetchPopularMovies(queries)
    }

    private suspend fun fetchPopularMovies(queries: Map<String, String>) {
        _popularMovies.postValue(Resource.Loading())

        try {
            if(Utils.hasInternetConnection(getApplication<MyApplication>())) {
                val response = movieRepository.getPopularMovies(queries)
                _popularMovies.postValue(handleMoviesResponse(response))
            } else {
                _popularMovies.postValue(Resource.Error("Network err"))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> _popularMovies.postValue(Resource.Error(t.message!!))
                else -> _popularMovies.postValue(Resource.Error(t.message!!))
            }
        }
    }

    private fun handleMoviesResponse(response: Response<Any>): Resource<MoviesResponse> {
        if(response.isSuccessful) {
            response.body()?.let {
                val gson = Gson()
                val json = gson.toJsonTree(it)
                val result =  gson.fromJson(
                    json.asJsonObject.get("results"),
                    MoviesResponse::class.java)

                return Resource.Success(result)
            }
        }

        return Resource.Error(response.message())
    }

    private suspend fun fetchMovieDetail(movieID: String, queries: Map<String, String>) {
        _movieDetail.postValue(Resource.Loading())

        try {
            if (Utils.hasInternetConnection(getApplication<MyApplication>())) {
                val response = movieRepository.getMovieDetail(movieID, queries)
                _movieDetail.postValue(handleMovieDetailResponse(response))
            } else {
                _movieDetail.postValue(Resource.Error("Network err"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _movieDetail.postValue(
                    Resource.Error(t.message!!)
                )
                else -> _movieDetail.postValue(
                    Resource.Error(t.message!!)
                )
            }
        }
    }

    private fun handleMovieDetailResponse(response: Response<MovieDetail>): Resource<MovieDetail> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        return Resource.Error(response.message())
    }
}