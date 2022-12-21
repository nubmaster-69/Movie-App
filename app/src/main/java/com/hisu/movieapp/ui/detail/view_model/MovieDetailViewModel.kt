package com.hisu.movieapp.ui.detail.view_model

import android.app.Application
import androidx.lifecycle.*
import com.google.gson.Gson
import com.hisu.movieapp.app.MyApplication
import com.hisu.movieapp.data.repository.MovieRepositoryService
import com.hisu.movieapp.model.CastsResponse
import com.hisu.movieapp.model.MovieDetail
import com.hisu.movieapp.model.RelatedMoviesResponse
import com.hisu.movieapp.utils.Resource
import com.hisu.movieapp.utils.Utils
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class MovieDetailViewModel(
    val app: Application,
    private val movieRepository: MovieRepositoryService
) : AndroidViewModel(app) {

    private val _relatedMovie = MutableLiveData<Resource<RelatedMoviesResponse>>()
    val relatedMovie: LiveData<Resource<RelatedMoviesResponse>>
        get() = _relatedMovie

    private val _movieDetail = MutableLiveData<Resource<MovieDetail>>()
    val movieDetail: LiveData<Resource<MovieDetail>>
        get() = _movieDetail

    private val _castsInfo = MutableLiveData<Resource<CastsResponse>>()
    val castsInfo: LiveData<Resource<CastsResponse>>
        get() = _castsInfo

    fun getMovieDetail(movieID: String, queries: Map<String, String>) = viewModelScope.launch {
        fetchMovieDetail(movieID, queries)
    }

    fun getRelatedMovies(movieID: String, queries: Map<String, String>) = viewModelScope.launch {
        fetchRelatedMovies(movieID, queries)
    }

    fun getCastsInfo(movieID: String) = viewModelScope.launch {
        fetchCastsInfo(movieID)
    }

    private suspend fun fetchCastsInfo(movieID: String) {
        _castsInfo.postValue(Resource.Loading())

        try {

            if(Utils.hasInternetConnection(getApplication<MyApplication>())) {
                val response = movieRepository.getCastsInfo(movieID)
                _castsInfo.postValue(handleCastsResponse(response))
            } else
                _castsInfo.postValue(Resource.Error("Network Error"))

        } catch(e: Exception) {
            _castsInfo.postValue(Resource.Error(e.message!!))
        }
    }

    private suspend fun fetchRelatedMovies(movieID: String, queries: Map<String, String>) {
        _relatedMovie.postValue(Resource.Loading())

        try {
            if(Utils.hasInternetConnection(getApplication<MyApplication>())) {
                val response = movieRepository.getRelatedMovies(movieID, queries)
                _relatedMovie.postValue(handleMoviesResponse(response))
            } else {
                _relatedMovie.postValue(Resource.Error("Network err"))
            }
        } catch(e: Exception) {
            _relatedMovie.postValue(Resource.Error(e.message!!))
        }
    }

    private fun handleMoviesResponse(response: Response<Any>): Resource<RelatedMoviesResponse> {
        if(response.isSuccessful) {
            response.body()?.let {
                val gson = Gson()
                val json = gson.toJsonTree(it)
                val result =  gson.fromJson(
                    json.asJsonObject.get("results"),
                    RelatedMoviesResponse::class.java)

                return Resource.Success(result)
            }
        }

        return Resource.Error(response.message())
    }

    private fun handleCastsResponse(response: Response<Any>): Resource<CastsResponse> {
        if(response.isSuccessful) {
            response.body()?.let {
                val gson = Gson()
                val json = gson.toJsonTree(it)
                val result =  gson.fromJson(
                    json.asJsonObject.get("cast"),
                    CastsResponse::class.java)

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