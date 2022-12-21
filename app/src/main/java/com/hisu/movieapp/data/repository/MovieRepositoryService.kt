package com.hisu.movieapp.data.repository

import com.hisu.movieapp.model.MovieDetail
import retrofit2.Response

interface MovieRepositoryService {
    suspend fun getPopularMovies(queries: Map<String, String>): Response<Any>
    suspend fun getMovieDetail(movieID: String, queries: Map<String, String>): Response<MovieDetail>
    suspend fun getRelatedMovies(movieID: String, queries: Map<String, String>): Response<Any>
    suspend fun getTrendingMovies(): Response<Any>
    suspend fun getCastsInfo(movieID: String): Response<Any>
    suspend fun searchMovie(queries: Map<String, String>): Response<Any>
}