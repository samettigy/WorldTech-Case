package com.android.orderapp.ui.fragments.favorite

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.orderapp.R
import com.android.orderapp.data.adapter.MovieAdapter
import com.android.orderapp.data.adapter.MovieAdapterInteraction
import com.android.orderapp.data.model.MovieModel
import com.android.orderapp.databinding.FragmentFavoritesBinding
import com.android.orderapp.ui.base.BaseFragment
import com.android.orderapp.ui.base.FragmentInflate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : BaseFragment<FavoritesViewModel, FragmentFavoritesBinding>(),
    MovieAdapterInteraction {

    override val viewModel: FavoritesViewModel by viewModels()
    override val viewBindingInflater: FragmentInflate<FragmentFavoritesBinding>
        get() = FragmentFavoritesBinding::inflate

    private var adapter: MovieAdapter? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewModel.screenState.observe(viewLifecycleOwner, Observer { screenState ->
            when (screenState) {
                is FavoritesScreenState.Content -> {
                    binding.contentView.visibility = View.VISIBLE
                    binding.loadingView.visibility = View.GONE
                    adapter = MovieAdapter(screenState.movies, screenState.movies, this)
                    val recyclerView = binding.rvFavoritesList
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                    recyclerView.adapter = adapter
                }

                is FavoritesScreenState.Error -> {
                    showToastMessage(screenState.error)
                    binding.contentView.visibility = View.GONE
                    binding.loadingView.visibility = View.GONE
                }

                FavoritesScreenState.Loading -> {
                    binding.contentView.visibility = View.GONE
                    binding.loadingView.visibility = View.VISIBLE
                }
            }
        })


    }

    override fun onFavoriteClick(movie: MovieModel, isChecked: Boolean) {
        if (isChecked.not()) {
            viewModel.deleteToFavorites(movie = movie)
        }
    }

    override fun onItemClick(itemId: String) {
        val bundle = bundleOf("id" to itemId)
        findNavController().navigate(R.id.detailFragment, bundle)
    }

}