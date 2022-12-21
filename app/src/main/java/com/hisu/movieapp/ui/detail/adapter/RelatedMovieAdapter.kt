package com.hisu.movieapp.ui.detail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hisu.movieapp.BuildConfig
import com.hisu.movieapp.R
import com.hisu.movieapp.databinding.LayoutRelatedMovieBinding
import com.hisu.movieapp.listener.IOnMovieItemClickListener
import com.hisu.movieapp.model.RelatedMovie

class RelatedMovieAdapter(val context: Context) : RecyclerView.Adapter<RelatedMovieAdapter.RelatedMovieViewHolder>() {

    var relatedMovies: List<RelatedMovie>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    var onMovieItemClickListener: IOnMovieItemClickListener? = null

    var diffCallback = object : DiffUtil.ItemCallback<RelatedMovie>() {
        override fun areItemsTheSame(oldItem: RelatedMovie, newItem: RelatedMovie): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: RelatedMovie, newItem: RelatedMovie): Boolean =
            oldItem.id == newItem.id
    }

    var differ = AsyncListDiffer(this, diffCallback)

    inner class RelatedMovieViewHolder(var binding: LayoutRelatedMovieBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedMovieViewHolder {
        return RelatedMovieViewHolder(
            LayoutRelatedMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RelatedMovieViewHolder, position: Int) {
        val relatedMovie = relatedMovies[position]

        holder.binding.apply {
            tvMovieName.text = relatedMovie.originalTitle
            Glide.with(context)
                .load(BuildConfig.POSTER_URL.plus(relatedMovie.posterPath))
                .placeholder(context.getDrawable(R.drawable.loading_image))
                .error(context.getDrawable(R.drawable.image_error))
                .into(imvCoverImage)

            imvCoverImage.setOnClickListener {
                onMovieItemClickListener?.itemClick(relatedMovie)
            }
        }
    }

    override fun getItemCount(): Int = relatedMovies.size
}