package com.hisu.movieapp.ui.see_all

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.hisu.movieapp.MainActivity
import com.hisu.movieapp.R
import com.hisu.movieapp.databinding.FragmentSeeAllBinding
import com.hisu.movieapp.listener.IOnMovieItemClickListener
import com.hisu.movieapp.model.MoviePreviewResult
import com.hisu.movieapp.ui.detail.MovieDetailFragment
import com.hisu.movieapp.ui.home.MovieAdapter
import com.hisu.movieapp.ui.home.MovieHomeFeatureAdapter
import com.hisu.movieapp.view_model.MovieViewModel

class SeeAllFragment : Fragment() {

    private lateinit var mainActivity: MainActivity
    private lateinit var mBinding: FragmentSeeAllBinding
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var movieViewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = FragmentSeeAllBinding.inflate(inflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(view)
        initMoviesList(view)

        val map = mutableMapOf<String, String>()
        map["language"] = "en-US"
        map["page"] = "1"

        movieViewModel = ViewModelProvider(mainActivity).get(MovieViewModel::class.java)
        movieViewModel.getPopularMovies(map).observe(mainActivity, Observer {
            movieAdapter.movies = it
            mBinding.rvAllMovies.adapter = movieAdapter
        })
    }

    private fun initToolbar(view: View) = mBinding.toolbar.setNavigationOnClickListener {
        Navigation.findNavController(view).popBackStack()
    }

    private fun initMoviesList(view: View) = mBinding.rvAllMovies.apply {
        movieAdapter = MovieAdapter()
        val gridLayoutManager =
            GridLayoutManager(mainActivity, 2, GridLayoutManager.VERTICAL, false)

        movieAdapter.onMovieItemClickListener = object : IOnMovieItemClickListener {
            override fun itemClick(movie: MoviePreviewResult) {
                val nav = Navigation.findNavController(view)
                val bundle = Bundle()
                bundle.putString(MovieDetailFragment.MOVIE_DETAIL_ARG, movie.id.toString())
                nav.navigate(R.id.all_movie_to_detail, bundle)
            }
        }

        adapter = movieAdapter
        layoutManager = gridLayoutManager
    }
}