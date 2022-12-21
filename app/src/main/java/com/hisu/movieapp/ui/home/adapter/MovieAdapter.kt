package com.hisu.movieapp.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hisu.movieapp.BuildConfig
import com.hisu.movieapp.R
import com.hisu.movieapp.databinding.LayoutMovieHomeItemBinding
import com.hisu.movieapp.listener.IOnMovieItemClickListener
import com.hisu.movieapp.model.MoviePreviewResult
import com.hisu.movieapp.utils.MyFormatUtils

class MovieAdapter(val context: Context) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    var movies: List<MoviePreviewResult>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    var onMovieItemClickListener: IOnMovieItemClickListener? = null

    inner class MovieViewHolder(var binding: LayoutMovieHomeItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object: DiffUtil.ItemCallback<MoviePreviewResult>() {
        override fun areItemsTheSame(
            oldItem: MoviePreviewResult,
            newItem: MoviePreviewResult
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: MoviePreviewResult,
            newItem: MoviePreviewResult
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

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

            Glide.with(context)
                .load(BuildConfig.POSTER_URL.plus(movie.posterPath))
                .placeholder(context.getDrawable(R.drawable.loading_image))
                .error(context.getDrawable(R.drawable.image_error))
                .into(imvMovieCoverImage)

            imvMovieCoverImage.setOnClickListener {
                onMovieItemClickListener?.itemClick(movie)
            }
        }
    }

    override fun getItemCount(): Int = movies.size
}