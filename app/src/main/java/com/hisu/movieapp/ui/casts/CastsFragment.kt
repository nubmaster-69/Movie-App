package com.hisu.movieapp.ui.casts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.hisu.movieapp.MainActivity
import com.hisu.movieapp.R
import com.hisu.movieapp.data.repository.impl.MovieRepositoryImpl
import com.hisu.movieapp.databinding.FragmentCastsBinding
import com.hisu.movieapp.model.Cast
import com.hisu.movieapp.ui.casts.adapter.CastsAdapter
import com.hisu.movieapp.ui.detail.view_model.MovieDetailViewModel
import com.hisu.movieapp.ui.home.view_model.MovieViewModelFactoryProvider
import com.hisu.movieapp.utils.Resource

class CastsFragment : Fragment() {

    private lateinit var mBinding: FragmentCastsBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var castsAdapter: CastsAdapter
    private lateinit var movieDetailViewModel: MovieDetailViewModel
    private var movieID: String = ""

    companion object {
        const val MOVIE_CAST_ARG: String = "MOVIE_CAST_ARG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity!! as MainActivity
        arguments?.let {
            movieID = arguments!!.getString(MOVIE_CAST_ARG,"")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentCastsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        setUpRecyclerView()
        initViewModel()
    }

    private fun initViewModel() {
        val movieRepository = MovieRepositoryImpl()
        val factory = MovieViewModelFactoryProvider(mainActivity.application, movieRepository)
        movieDetailViewModel =
            ViewModelProvider(mainActivity, factory).get(MovieDetailViewModel::class.java)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            movieDetailViewModel.getCastsInfo(movieID)
        }

        movieDetailViewModel.castsInfo.observe(mainActivity) {
            if(it is Resource.Success) {
                castsAdapter.casts = it.data as ArrayList<Cast>
                castsAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setUpRecyclerView() = mBinding.rvCasts.apply {
        castsAdapter = CastsAdapter(mainActivity)
        adapter = castsAdapter
        layoutManager = GridLayoutManager(mainActivity, 2, GridLayoutManager.VERTICAL, false)
    }

    private fun initToolbar() = mBinding.toolbar.setNavigationOnClickListener {
        findNavController().popBackStack()
    }
}