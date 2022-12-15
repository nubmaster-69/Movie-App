package com.hisu.movieapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hisu.movieapp.BuildConfig
import com.hisu.movieapp.databinding.LayoutMovieHomeFeatureBinding
import com.hisu.movieapp.listener.IOnMovieItemClickListener
import com.hisu.movieapp.model.MoviePreviewResult

class MovieHomeFeatureAdapter :
    RecyclerView.Adapter<MovieHomeFeatureAdapter.MovieFeatureViewHolder>() {

    var featureMovies: List<MoviePreviewResult> = mutableListOf()
    lateinit var onMovieItemClickListener: IOnMovieItemClickListener

    inner class MovieFeatureViewHolder(var binding: LayoutMovieHomeFeatureBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieFeatureViewHolder {
        return MovieFeatureViewHolder(
            LayoutMovieHomeFeatureBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieFeatureViewHolder, position: Int) {
        val movie = featureMovies[position]
        holder.binding.apply {

            Glide.with(imvMovieFeature)
                .load(BuildConfig.POSTER_URL + movie.posterPath)
                .into(imvMovieFeature)

            parentContainer.setOnClickListener {
                if (onMovieItemClickListener != null)
                    onMovieItemClickListener.itemClick(movie)
            }

        }
    }

    override fun getItemCount(): Int = featureMovies.size
}