package com.hisu.movieapp.data.repository.impl

import API
import com.hisu.movieapp.data.repository.MovieRepositoryService

class MovieRepositoryImpl : MovieRepositoryService {
    override suspend fun getPopularMovies(queries: Map<String, String>) =
        API.apiService.getPopularMovies(queries)

    override suspend fun getMovieDetail(movieID: String, queries: Map<String, String>) =
        API.apiService.getMovieDetail(movieID, queries)

    override suspend fun getRelatedMovies(movieID: String, queries: Map<String, String>) =
        API.apiService.getRelatedMovies(movieID, queries)

    override suspend fun getTrendingMovies() = API.apiService.getTrendingMovies()

    override suspend fun getCastsInfo(movieID: String) = API.apiService.getCastsInfo(movieID)

    override suspend fun searchMovie(queries: Map<String, String>) =
        API.apiService.searchMovie(queries)
}