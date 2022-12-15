package com.hisu.movieapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hisu.movieapp.BuildConfig
import com.hisu.movieapp.R
import com.hisu.movieapp.databinding.LayoutMovieHomeItemBinding
import com.hisu.movieapp.listener.IOnMovieItemClickListener
import com.hisu.movieapp.model.MoviePreviewResult
import com.hisu.movieapp.utils.MyFormatUtils
import java.lang.Math.round
import java.text.SimpleDateFormat
import java.util.*

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    var movies: List<MoviePreviewResult> = mutableListOf()
    lateinit var onMovieItemClickListener: IOnMovieItemClickListener

    inner class MovieViewHolder(var binding: LayoutMovieHomeItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutMovieHomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]

        holder.binding.apply {
            tvMovieName.text = movie.originalTitle
            tvMovieReleaseYear.text = MyFormatUtils.dateFormat(movie.releaseDate)
            tvMovieRate.text = String.format("%.1f", movie.voteAverage)

            Glide.with(imvMovieCoverImage)
                .load(BuildConfig.POSTER_URL + movie.posterPath)
                .into(imvMovieCoverImage)

            imvMovieCoverImage.setOnClickListener {
                if (onMovieItemClickListener != null)
                    onMovieItemClickListener.itemClick(movie)
            }
        }
    }

    override fun getItemCount(): Int = movies.size
}