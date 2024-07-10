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
import com.android.orderapp.di.imageBase
import com.bumptech.glide.Glide

interface MovieAdapterInteraction {
    fun onFavoriteClick(movie: MovieModel, isChecked: Boolean)
    fun onItemClick(itemId: String)
}

class MovieAdapter(
    var moviesList: List<MovieModel>,
    var favorites: List<MovieModel>,
    private val interaction: MovieAdapterInteraction
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {


    class MovieViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var moviePoster: ImageView = view.findViewById(R.id.imgMovie)
        val movieTitle: TextView = view.findViewById(R.id.tvMovieName)
        val movieRate: TextView = view.findViewById(R.id.tvRate)
        val movieLang: TextView = view.findViewById(R.id.tvLang)
        val price: TextView = view.findViewById(R.id.txtPrice)
        val movieReleaseDate: TextView = view.findViewById(R.id.tvMovieDateRelease)
        val cbFav: CheckBox = view.findViewById(R.id.cbFav)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentItem = moviesList[position]
        val inFavorites = favorites.any { favorite -> favorite.id?.equals(currentItem.id) == true }

        holder.itemView.setOnClickListener {
            interaction.onItemClick(currentItem.id.toString())
        }

        holder.cbFav.setOnCheckedChangeListener { buttonView, isChecked ->
            interaction.onFavoriteClick(currentItem, isChecked)
        }

        Glide.with(holder.itemView)
            .load(imageBase + currentItem.posterPath)
            .into(holder.moviePoster)

        holder.movieRate.text = currentItem.voteAverage.toString()
        holder.movieLang.text = currentItem.originalLanguage
        holder.movieTitle.text = currentItem.title
        holder.movieReleaseDate.text = currentItem.releaseDate
        holder.cbFav.isChecked = inFavorites
        holder.price.text = (20..200).random().toString()
    }

}