package com.hisu.movieapp.ui.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hisu.movieapp.databinding.LayoutGenreBinding
import com.hisu.movieapp.model.Genre

class GenreAdapter: RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    var genres = listOf<Genre>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        return GenreViewHolder(
            LayoutGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genre = genres[position]

        holder.binding.apply {
            tvGenre.text = genre.name
        }
    }

    override fun getItemCount() = genres.size

    inner class GenreViewHolder(var binding: LayoutGenreBinding): RecyclerView.ViewHolder(binding.root)
}
