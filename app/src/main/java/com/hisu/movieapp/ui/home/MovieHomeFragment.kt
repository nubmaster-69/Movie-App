package com.hisu.movieapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.hisu.movieapp.MainActivity
import com.hisu.movieapp.R
import com.hisu.movieapp.data.repository.impl.MovieRepositoryImpl
import com.hisu.movieapp.databinding.FragmentMovieHomeBinding
import com.hisu.movieapp.listener.IOnMovieItemClickListener
import com.hisu.movieapp.model.MoviePreviewResult
import com.hisu.movieapp.ui.detail.MovieDetailFragment
import com.hisu.movieapp.ui.home.adapter.MovieAdapter
import com.hisu.movieapp.ui.home.adapter.MovieHomeFeatureAdapter
import com.hisu.movieapp.utils.Resource
import com.hisu.movieapp.view_model.MovieHomeViewModel
import com.hisu.movieapp.view_model.MovieViewModelFactoryProvider
import kotlin.math.abs

class MovieHomeFragment : Fragment() {

    private lateinit var mainActivity: MainActivity
    private lateinit var mBinding: FragmentMovieHomeBinding
    private lateinit var movieAdapter: MovieHomeFeatureAdapter
    private lateinit var popularMoviesAdapter: MovieAdapter
    private lateinit var movieViewModel: MovieHomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentMovieHomeBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFeatureMovieList()
        initMoviesList()
        viewAllMovies(view)
        setupViewModel()
    }

    private fun setupViewModel() {
        val movieRepository = MovieRepositoryImpl()
        val factory = MovieViewModelFactoryProvider(mainActivity.application, movieRepository)
        movieViewModel = ViewModelProvider(mainActivity, factory)[MovieHomeViewModel::class.java]

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val map = mutableMapOf<String, String>()
            map["language"] = "en-US"
            map["page"] = "1"

            movieViewModel.getPopularMovies(map)
        }

        movieViewModel.popularMovie.observe(mainActivity) {
            if(it is Resource.Success) {
                movieAdapter.featureMovies = it.data as ArrayList<MoviePreviewResult>
                mBinding.vpFeatureMovie.adapter = movieAdapter

                popularMoviesAdapter.movies = it.data as ArrayList<MoviePreviewResult>
                mBinding.rvMoviesList.adapter = popularMoviesAdapter
            }
        }
    }

    private fun initFeatureMovieList() = mBinding.vpFeatureMovie.apply {
        movieAdapter = MovieHomeFeatureAdapter()

        val transformer = CompositePageTransformer()

        transformer.addTransformer(MarginPageTransformer(80))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }

        clipToPadding = false
        clipChildren = false
        offscreenPageLimit = 3

        movieAdapter.onMovieItemClickListener = object : IOnMovieItemClickListener {
            override fun itemClick(movie: MoviePreviewResult) {
                val bundle = Bundle()
                bundle.putString(MovieDetailFragment.MOVIE_DETAIL_ARG, movie.id.toString())
                findNavController().navigate(R.id.movie_to_detail, bundle)
            }
        }

        adapter = movieAdapter

        setPageTransformer(transformer)
    }

    private fun initMoviesList() = mBinding.rvMoviesList.apply {
        popularMoviesAdapter = MovieAdapter()
        val gridLayoutManager =
            GridLayoutManager(mainActivity, 2, GridLayoutManager.VERTICAL, false)

        popularMoviesAdapter.onMovieItemClickListener = object : IOnMovieItemClickListener {
            override fun itemClick(movie: MoviePreviewResult) {
                val bundle = Bundle()
                bundle.putString(MovieDetailFragment.MOVIE_DETAIL_ARG, movie.id.toString())
                findNavController().navigate(R.id.movie_to_detail, bundle)
            }
        }

        adapter = popularMoviesAdapter
        layoutManager = gridLayoutManager
        isNestedScrollingEnabled = false
        setHasFixedSize(true)
    }

    private fun viewAllMovies(view: View) = mBinding.tvSeeAll.setOnClickListener {
        findNavController().navigate(R.id.home_to_all_movie)
    }
}