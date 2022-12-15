package com.hisu.movieapp.listener

import com.hisu.movieapp.model.MoviePreviewResult

interface IOnMovieItemClickListener {
    fun itemClick(movie: MoviePreviewResult)
}