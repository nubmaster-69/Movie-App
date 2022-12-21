package com.hisu.movieapp.model


import com.google.gson.annotations.SerializedName

data class RelatedMovie(
    @SerializedName("backdrop_path")
    val backdropPath: String,
    val id: Int,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("poster_path")
    val posterPath: String
)