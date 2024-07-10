package com.android.orderapp.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.orderapp.R
import com.android.orderapp.data.model.MovieModel
import com.bumptech.glide.Glide

interface CartAdapterInteraction {
    fun onFavoriteClick(movie: MovieModel, isChecked: Boolean)
    fun onItemClick(itemId: String)
}

class CartAdapter(
    var cartMovieList : List<MovieModel>,
    private val interaction: CartAdapterInteraction
) : RecyclerView.Adapter<CartAdapter.FavoritesViewHolder>() {

    private var imageBase: String = "https://image.tmdb.org/t/p/w500/"

    class FavoritesViewHolder(var view : View) : RecyclerView.ViewHolder(view) {
        var moviePoster : ImageView = view.findViewById(R.id.imgMovie)
        val movieTitle: TextView = view.findViewById(R.id.tvMovieName)
        val movieRate: TextView = view.findViewById(R.id.tvRate)
        val movieLang: TextView = view.findViewById(R.id.tvLang)
        val movieReleaseDate: TextView = view.findViewById(R.id.tvMovieDateRelease)
        val cbDelete : CheckBox = view.findViewById(R.id.cbDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item_basket,parent,false)
        return FavoritesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cartMovieList.size
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val currentItem = cartMovieList[position]

        holder.itemView.setOnClickListener {
            interaction.onItemClick(currentItem.id.toString())
        }

        holder.cbDelete.setOnCheckedChangeListener { buttonView, isChecked ->
            interaction.onFavoriteClick(currentItem, isChecked)
        }

        Glide.with(holder.itemView)
            .load(imageBase + currentItem.posterPath)
            .into(holder.moviePoster)

        holder.movieRate.text = currentItem.voteAverage.toString()
        holder.movieLang.text = currentItem.originalLanguage
        holder.movieTitle.text = currentItem.title
        holder.movieReleaseDate.text = currentItem.releaseDate

    }

}