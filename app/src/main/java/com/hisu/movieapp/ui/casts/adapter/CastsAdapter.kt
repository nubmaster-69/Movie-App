package com.hisu.movieapp.ui.casts.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hisu.movieapp.BuildConfig
import com.hisu.movieapp.R
import com.hisu.movieapp.databinding.LayoutCastBinding
import com.hisu.movieapp.databinding.LayoutCastItemBinding
import com.hisu.movieapp.model.Cast

class CastsAdapter(val context: Context) : RecyclerView.Adapter<CastsAdapter.CastViewHolder>() {

    var casts: List<Cast>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    val diffCallback = object : DiffUtil.ItemCallback<Cast>() {
        override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem.castId == newItem.castId
        }

        override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem.castId == newItem.castId
        }
    }

    var differ = AsyncListDiffer(this, diffCallback)

    inner class CastViewHolder(val binding: LayoutCastItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        return CastViewHolder(
            LayoutCastItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val cast = casts[position]
        holder.binding.apply {
            Glide.with(context)
                .load(BuildConfig.POSTER_URL.plus(cast.profilePath))
                .placeholder(context.getDrawable(R.drawable.loading_image))
                .error(context.getDrawable(R.drawable.image_error))
                .into(imvCastPfp)

            tvCastName.text = cast.originalName
            tvCastAs.text = cast.character
        }
    }

    override fun getItemCount(): Int = casts.size
}