package com.hisu.movieapp.ui.see_all

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.postDelayed
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.style.Wave
import com.hisu.movieapp.MainActivity
import com.hisu.movieapp.R
import com.hisu.movieapp.data.repository.impl.MovieRepositoryImpl
import com.hisu.movieapp.databinding.FragmentSeeAllBinding
import com.hisu.movieapp.listener.IOnMovieItemClickListener
import com.hisu.movieapp.model.MoviePreviewResult
import com.hisu.movieapp.ui.detail.MovieDetailFragment
import com.hisu.movieapp.ui.home.adapter.MovieAdapter
import com.hisu.movieapp.view_model.MovieHomeViewModel
import com.hisu.movieapp.view_model.MovieViewModelFactoryProvider

class SeeAllFragment : Fragment() {

    private lateinit var mainActivity: MainActivity
    private lateinit var mBinding: FragmentSeeAllBinding
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var movieViewModel: MovieHomeViewModel
    private lateinit var gridLayoutManager: GridLayoutManager

    private var page = 1
    private var movies = mutableListOf<MoviePreviewResult>()
    private var loading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentSeeAllBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val wave = Wave()
        mBinding.pbLoading.indeterminateDrawable = wave
        initToolbar()
        initMoviesList()
        backToTopEvent()
        setupViewModel()
    }

    private fun backToTopEvent() = mBinding.btnBackToTop.setOnClickListener {
        mBinding.rvAllMovies.smoothScrollToPosition(0)
    }

    private fun setupViewModel() {
        val movieRepository = MovieRepositoryImpl()
        val factory = MovieViewModelFactoryProvider(mainActivity.application, movieRepository)

        movieViewModel = ViewModelProvider(mainActivity, factory)[MovieHomeViewModel::class.java]

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            loadPage(page)
        }

        movieViewModel.popularMovie.observe(mainActivity) {
            if (it is com.hisu.movieapp.utils.Resource.Success) {
                movies.addAll(it.data as ArrayList<MoviePreviewResult>)

                Handler(mainActivity.mainLooper).postDelayed(3 * 1000) {
                    mBinding.rvAllMovies.visibility = View.VISIBLE
                    mBinding.pbLoading.visibility = View.GONE

                    movieAdapter.movies = movies

                    if (::movieAdapter.isInitialized)
                        movieAdapter.notifyDataSetChanged()
                    else
                        mBinding.rvAllMovies.adapter = movieAdapter

                    loading = false
                }

            } else if (it is com.hisu.movieapp.utils.Resource.Loading) {
                mBinding.pbLoading.visibility = View.VISIBLE
            }
        }
    }

    private fun loadPage(page: Int) {
        loading = true
        mBinding.pbLoading.visibility = View.VISIBLE

        val map = mutableMapOf<String, String>()
        map["language"] = "en-US"
        map["page"] = "$page"

        movieViewModel.getPopularMovies(map)
    }

    private fun initToolbar() = mBinding.toolbar.setNavigationOnClickListener {
        findNavController().popBackStack()
    }

    private fun initMoviesList() = mBinding.rvAllMovies.apply {
        movieAdapter = MovieAdapter()
        gridLayoutManager = GridLayoutManager(mainActivity, 2, GridLayoutManager.VERTICAL, false)

        movieAdapter.onMovieItemClickListener = object : IOnMovieItemClickListener {
            override fun itemClick(movie: MoviePreviewResult) {
                val bundle = Bundle()
                bundle.putString(MovieDetailFragment.MOVIE_DETAIL_ARG, movie.id.toString())
                findNavController().navigate(R.id.all_movie_to_detail, bundle)
            }
        }

        adapter = movieAdapter
        layoutManager = gridLayoutManager

        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    if (gridLayoutManager.findFirstVisibleItemPosition() > 5)
                        mBinding.btnBackToTop.show()

                    if (!loading && page < 5 && (gridLayoutManager.childCount + gridLayoutManager.findFirstVisibleItemPosition())
                        >= gridLayoutManager.itemCount
                    ) loadPage(++page)
                } else {
                    mBinding.btnBackToTop.hide()
                }
            }
        })
    }
}