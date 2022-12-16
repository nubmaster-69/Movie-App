package com.hisu.movieapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.hisu.movieapp.BuildConfig
import com.hisu.movieapp.MainActivity
import com.hisu.movieapp.R
import com.hisu.movieapp.databinding.FragmentMovieDetailBinding
import com.hisu.movieapp.utils.MyFormatUtils
import com.hisu.movieapp.view_model.MovieViewModel

class MovieDetailFragment : Fragment() {

    private lateinit var mainActivity: MainActivity
    private lateinit var mBinding: FragmentMovieDetailBinding
    private var movieID: String = ""
    private lateinit var movieViewModel: MovieViewModel

    companion object {
        val MOVIE_DETAIL_ARG: String = "MOVIE_DETAIL_ARG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity!! as MainActivity
        arguments?.let {
            movieID = arguments!!.getString(MOVIE_DETAIL_ARG).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backToHomeScreen(view);

        val queries = mutableMapOf<String, String>()
        queries["language"] = "en-US"

        movieViewModel = ViewModelProvider(mainActivity).get(MovieViewModel::class.java)
        movieViewModel.getMovieDetail(movieID, queries)
            .observe(mainActivity, Observer { movieDetail ->

                val movie = movieDetail!!

                mBinding.apply {

                    Glide.with(imageView)
                        .load(BuildConfig.POSTER_URL + movie.posterPath)
                        .into(imageView)

                    tvMovieName.text = movie.originalTitle
                    tvRate.text = MyFormatUtils.ratingFormat(movie.voteAverage)
                    tvVoteTotal.text = MyFormatUtils.voteCountFormat(movie.voteCount)
                    tvReleaseDate.text = MyFormatUtils.dateFormat(movie.releaseDate)
                    tvBudget.text = movie.budget
                    tvOverview.text = movie.overview
                    tvRuntime.text = MyFormatUtils.runtimeFormat(movie.runtime)

                    rvGenre.apply {
                        val genreAdapter = GenreAdapter()
                        genreAdapter.genres = movie.genres
                        adapter = genreAdapter
                        layoutManager =
                            LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false)
                    }
                }
            })

        if (movieID.isNotEmpty()) {
            movieViewModel.setMovieID(movieID)
        }
    }

    private fun backToHomeScreen(view: View) = mBinding.btnBack.setOnClickListener {
        val nav = Navigation.findNavController(view)
        nav.popBackStack()
    }
}