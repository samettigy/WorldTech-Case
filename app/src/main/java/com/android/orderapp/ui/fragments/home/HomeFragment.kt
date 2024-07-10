package com.android.orderapp.ui.fragments.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.CheckBox
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.orderapp.R
import com.android.orderapp.data.adapter.MovieAdapter
import com.android.orderapp.data.adapter.MovieAdapterInteraction
import com.android.orderapp.data.model.MovieModel
import com.android.orderapp.databinding.FragmentHomeBinding
import com.android.orderapp.ui.base.BaseFragment
import com.android.orderapp.ui.base.FragmentInflate
import com.android.orderapp.ui.fragments.details.DetailFragment
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(), MovieAdapterInteraction {
    override val viewModel: HomeViewModel by viewModels()
    override val viewBindingInflater: FragmentInflate<FragmentHomeBinding>
        get() = FragmentHomeBinding::inflate

    private var adapter: MovieAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        /*
        binding.searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                val filteredMovies = viewModel.movieList.value?.filter {
                    it.title!!.contains(query ?: "",ignoreCase = true)
                }
                adapter.moviesList.clear()
                adapter.moviesList.addAll(filteredMovies ?: emptyList())
                adapter.notifyDataSetChanged()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredMovies = viewModel.movieList.value?.filter {
                    it.title!!.contains(newText ?: "",ignoreCase = true)
                }
                adapter.moviesList.clear()
                adapter.moviesList.addAll(filteredMovies ?: emptyList())
                adapter.notifyDataSetChanged()
                return true
            }
        })

         */


        val recyclerView = binding.rvMovieList
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        viewModel.movieList.observe(viewLifecycleOwner, Observer { movies ->
            if (adapter == null) {
                adapter =
                    MovieAdapter(
                        movies as ArrayList<MovieModel>,
                        viewModel.favorites.value.orEmpty(),
                        this
                    )
                recyclerView.adapter = adapter
            } else {
                adapter?.moviesList = movies as ArrayList<MovieModel>
                adapter?.notifyDataSetChanged()
            }
        })

        viewModel.favorites.observe(viewLifecycleOwner, Observer { favorites ->
            if (adapter == null) {
                adapter =
                    MovieAdapter(
                        viewModel.movieList.value.orEmpty(),
                        favorites,
                        this
                    )
                recyclerView.adapter = adapter
            } else {
                adapter?.favorites = favorites
                adapter?.notifyDataSetChanged()
            }

        })

        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                val alertDialogBuilder = AlertDialog.Builder(requireContext())
                alertDialogBuilder.setMessage("Do you want to exit the application?")
                alertDialogBuilder.setPositiveButton("Evet") { _, _ ->
                    requireActivity().finish()
                }
                alertDialogBuilder.setNegativeButton("Hayır") { _, _ ->

                }
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
                true
            } else {
                false
            }
        }


    }


    override fun onFavoriteClick(movie: MovieModel, isChecked: Boolean) {
        viewModel.updateFavoriteStatus(movie)
    }

    override fun onItemClick(itemId: String) {
        val bundle = bundleOf("id" to itemId)
        findNavController().navigate(R.id.detailFragment, bundle)
    }
}