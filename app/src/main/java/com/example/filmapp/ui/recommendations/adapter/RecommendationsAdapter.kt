package com.example.filmapp.ui.recommendations.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.filmapp.databinding.FilmItemBinding
import com.example.filmapp.databinding.RecommendationsItemBinding
import com.example.filmapp.domain.entity.FilmDetail
import com.example.filmapp.ui.common.UserState

class RecommendationsAdapter(
    private val filmsAndUsers: List<Pair<FilmDetail, UserState>>,
    private val onClick: () -> Unit
) : RecyclerView.Adapter<RecommendationsAdapter.RecommendationsViewHolder>() {

    inner class RecommendationsViewHolder(private val binding: RecommendationsItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind (url: String, login: String, title: String, year: Int){
            binding.apply {
                tvYear.text = year.toString()
                tvTitle.text = title
                tvFrom.text = login
                Glide.with(binding.ivPreview).load(url).error(Color.Gray).apply(RequestOptions().centerCrop()).into(binding.ivPreview)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationsViewHolder {
        return RecommendationsViewHolder(
            RecommendationsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecommendationsViewHolder, position: Int) {
        holder.bind(filmsAndUsers[position].first.posterUrl, filmsAndUsers[position].second.username, filmsAndUsers[position].first.title, filmsAndUsers[position].first.year)
    }

    override fun getItemCount(): Int = filmsAndUsers.size
}