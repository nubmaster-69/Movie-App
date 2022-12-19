package com.hisu.movieapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.hisu.movieapp.BuildConfig
import com.hisu.movieapp.MainActivity
import com.hisu.movieapp.data.repository.impl.MovieRepositoryImpl
import com.hisu.movieapp.databinding.FragmentMovieDetailBinding
import com.hisu.movieapp.model.Genre
import com.hisu.movieapp.model.MovieDetail
import com.hisu.movieapp.ui.detail.adapter.GenreAdapter
import com.hisu.movieapp.utils.MyFormatUtils
import com.hisu.movieapp.utils.Resource
import com.hisu.movieapp.view_model.MovieHomeViewModel
import com.hisu.movieapp.view_model.MovieViewModelFactoryProvider

class MovieDetailFragment : Fragment() {

    private lateinit var mainActivity: MainActivity
    private lateinit var mBinding: FragmentMovieDetailBinding
    private var movieID: String = ""
    private lateinit var movieDetailViewModel: MovieHomeViewModel

    companion object {
        const val MOVIE_DETAIL_ARG: String = "MOVIE_DETAIL_ARG"
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
    ): View {
        mBinding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backToHomeScreen()
        setupViewModel()
    }

    private fun setupViewModel() {
        val movieRepository = MovieRepositoryImpl()
        val factory = MovieViewModelFactoryProvider(mainActivity.application, movieRepository)
        movieDetailViewModel =
            ViewModelProvider(mainActivity, factory).get(MovieHomeViewModel::class.java)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val queries = mutableMapOf<String, String>()
            queries["language"] = "en-US"
            movieDetailViewModel.getMovieDetail(movieID, queries)
        }

        movieDetailViewModel.movieDetail.observe(mainActivity) {
            if (it is Resource.Success) {
                mBinding.pbDetailLoading.visibility = View.GONE
                mBinding.movieDetailContainer.visibility = View.VISIBLE

                setMovieDetail(it.data!!)

            } else if (it is Resource.Loading) {
                mBinding.pbDetailLoading.visibility = View.VISIBLE
            }
        }
    }

    private fun setMovieDetail(movieDetailResult: MovieDetail) = mBinding.apply {
        Glide.with(imageView)
            .load(BuildConfig.POSTER_URL + movieDetailResult.posterPath)
            .into(imageView)

        tvMovieName.text = movieDetailResult.originalTitle
        tvRate.text = MyFormatUtils.ratingFormat(movieDetailResult.voteAverage)
        tvVoteTotal.text = MyFormatUtils.voteCountFormat(movieDetailResult.voteCount)
        tvReleaseDate.text = MyFormatUtils.dateFormat(movieDetailResult.releaseDate)
        tvBudget.text = movieDetailResult.budget
        tvOverview.text = movieDetailResult.overview
        tvRuntime.text = MyFormatUtils.runtimeFormat(movieDetailResult.runtime)

        setGenre(movieDetailResult.genres)
    }

    private fun setGenre(genres: List<Genre>) = mBinding.rvGenre.apply {
        val genreAdapter = GenreAdapter()
        genreAdapter.genres = genres
        adapter = genreAdapter
        layoutManager = LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun backToHomeScreen() = mBinding.btnBack.setOnClickListener {
        findNavController().popBackStack()
    }
}