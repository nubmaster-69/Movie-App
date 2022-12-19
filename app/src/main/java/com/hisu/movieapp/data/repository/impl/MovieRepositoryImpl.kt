package com.hisu.movieapp.data.repository.impl

import API
import com.hisu.movieapp.data.repository.MovieRepositoryService

class MovieRepositoryImpl : MovieRepositoryService {

    override suspend fun getPopularMovies(queries: Map<String, String>) =
        API.apiService.getPopularMovies(queries)

    override suspend fun getMovieDetail(
        movieID: String, queries: Map<String, String>
    ) = API.apiService.getMovieDetail(movieID, queries)
}