package com.hisu.movieapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.hisu.movieapp.model.MovieDetail
import com.hisu.movieapp.model.MoviePreviewResult
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

object MovieRepository {

    var job: CompletableJob? = null

    fun getPopularMovies(queries: Map<String, String>): LiveData<List<MoviePreviewResult>> {
        job = Job()
        return object : LiveData<List<MoviePreviewResult>>() {
            override fun onActive() {
                super.onActive()
                job?.let { job ->
                    CoroutineScope(IO + job).launch {
                        val movies = API.apiService.getPopularMovies(queries)
                        withContext(Main) {
                            val gson = Gson()

                            val json = gson.toJsonTree(movies)
                            value =  gson.fromJson(
                                json.asJsonObject.get("results"),
                                Array<MoviePreviewResult>::class.java).toList()

                            job.complete()
                        }
                    }
                }
            }
        }
    }

    fun getMovieDetail(movieID: String, queries: Map<String, String>): LiveData<MovieDetail> {
        job = Job()
        return object : LiveData<MovieDetail>() {
            override fun onActive() {
                super.onActive()
                job?.let { job ->
                    CoroutineScope(IO + job).launch {
                        val movie = API.apiService.getMovieDetail(movieID, queries)
                        withContext(Main) {
                            value = movie
                            job.complete()
                        }
                    }
                }
            }
        }
    }

    fun cancelJob() {
        job?.cancel()
    }
}