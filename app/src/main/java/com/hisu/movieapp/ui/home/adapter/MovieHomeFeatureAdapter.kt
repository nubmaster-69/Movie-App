package com.hisu.movieapp.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hisu.movieapp.BuildConfig
import com.hisu.movieapp.R
import com.hisu.movieapp.databinding.LayoutMovieHomeFeatureBinding
import com.hisu.movieapp.listener.IOnMovieItemClickListener
import com.hisu.movieapp.model.MoviePreviewResult

class MovieHomeFeatureAdapter(val context: Context) :
    RecyclerView.Adapter<MovieHomeFeatureAdapter.MovieFeatureViewHolder>() {

    var featureMovies: List<MoviePreviewResult> = mutableListOf()
    var onMovieItemClickListener: IOnMovieItemClickListener? = null

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

            Glide.with(context)
                .load(BuildConfig.POSTER_URL + movie.posterPath)
                .placeholder(context.getDrawable(R.drawable.loading_image))
                .error(context.getDrawable(R.drawable.image_error))
                .into(imvMovieFeature)

            parentContainer.setOnClickListener {
                onMovieItemClickListener?.itemClick(movie)
            }
        }
    }

    override fun getItemCount(): Int = featureMovies.size
}