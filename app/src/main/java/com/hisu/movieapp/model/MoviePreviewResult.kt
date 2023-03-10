package com.hisu.movieapp.model


import com.google.gson.annotations.SerializedName

data class MoviePreviewResult(
    @SerializedName("backdrop_path")
    val backdropPath: String,
    val id: Int,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("media_type")
    val mediaType: String?,
    @SerializedName("vote_average")
    val voteAverage: Double
)