package com.android.orderapp.ui.fragments.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.android.orderapp.data.model.MovieModel
import com.android.orderapp.databinding.FragmentDetailBinding
import com.android.orderapp.di.imageBase
import com.android.orderapp.ui.base.BaseFragment
import com.android.orderapp.ui.base.FragmentInflate
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailFragment : BaseFragment<DetailViewModel, FragmentDetailBinding>() {

    override val viewModel: DetailViewModel by viewModels()
    override val viewBindingInflater: FragmentInflate<FragmentDetailBinding>
        get() = FragmentDetailBinding::inflate


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val movieId = arguments?.getString("id")
        movieId?.let { viewModel.getMovieDetails(it.toInt()) }


        binding.btnAddToCart.setOnClickListener {
            viewModel.updateMovieBasketStatus()
        }

        viewModel.basketState.observe(viewLifecycleOwner, Observer { inBasket ->
            if (inBasket) {
                binding.btnAddToCart.text = "Sepetten Kaldır"
            } else {
                binding.btnAddToCart.text = "Sepete Ekle"
            }
        })

        viewModel.libraryState.observe(viewLifecycleOwner, Observer { inLibrary ->
            if (inLibrary) {

                binding.btnAddToCart.visibility = View.GONE
                binding.lyUpdateReview.visibility = View.VISIBLE
            } else {
                binding.btnAddToCart.visibility = View.VISIBLE
                binding.lyUpdateReview.visibility = View.GONE
            }

        })


        viewModel.movieDetails.observe(viewLifecycleOwner, Observer { movieDetails ->

            Glide.with(this)
                .load(imageBase + movieDetails.posterPath)
                .apply(RequestOptions().centerCrop())
                .into(binding.imgMovie)

            Glide.with(this)
                .load(imageBase + movieDetails.posterPath)
                .apply(RequestOptions().centerCrop())
                .into(binding.imgMovieBack)

            binding.tvMovieTitle.text = movieDetails.title
            binding.txtPrice.text = "${(10..250).random()} ₺"
            binding.tvMovieDateRelease.text = movieDetails.releaseDate
            binding.tvMovieRating.text = movieDetails.voteAverage.toString()
            binding.etMovieReview.setText(movieDetails.review)

        })


        viewModel.favoritesState.observe(viewLifecycleOwner, Observer { isFavorite ->
            binding.cbFav.isChecked = isFavorite ?: false
        })

        binding.cbFav.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.updateFavoriteStatus(isChecked)
        }

        binding.btnUpdateReview.setOnClickListener {
            viewModel.updateMovieReview(binding.etMovieReview.text.toString())
        }


    }
}