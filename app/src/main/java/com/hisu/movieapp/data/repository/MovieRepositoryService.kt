package com.hisu.movieapp.data.repository

import com.hisu.movieapp.model.MovieDetail
import retrofit2.Response

interface MovieRepositoryService {
    suspend fun getPopularMovies(queries: Map<String, String>): Response<Any>
    suspend fun getMovieDetail(movieID: String, queries: Map<String, String>): Response<MovieDetail>
}