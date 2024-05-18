package com.example.filmapp.ui.list.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.filmapp.R
import com.example.filmapp.databinding.FilmItemBinding
import com.example.filmapp.domain.entity.Film
import com.example.filmapp.ui.detailed.DetailFragment
import com.example.filmapp.ui.detailed.DetailViewModel


class FilmListAdapter(
    private val films: List<Film>,
    private val viewModel: DetailViewModel,
    private val context: Context
) : RecyclerView.Adapter<FilmListAdapter.FilmsViewHolder>() {

    inner class FilmsViewHolder(private val binding: FilmItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(title: String, rating: Double, url: String, year: Int, id: String){
            val manager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
            binding.root.setOnClickListener {
                viewModel.id.value = id
                viewModel.getFilm()
                val fragmentTransaction = manager.beginTransaction()
                fragmentTransaction.setReorderingAllowed(true).setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                fragmentTransaction.replace(R.id.container, DetailFragment())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }

            binding.tvTitle.text = title
            binding.tvYear.text = year.toString()
            binding.ratingBar.rating = (rating/2).toFloat()
            Log.i("RATING", (rating/2).toFloat().toString())
            Glide.with(binding.ivPreview).load(url).error(Color.Gray).apply(RequestOptions().centerCrop()).into(binding.ivPreview)
        }

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FilmsViewHolder {
        return FilmsViewHolder(
            FilmItemBinding.inflate(
                LayoutInflater.from(p0.context),
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return films.size
    }

    override fun onBindViewHolder(p0: FilmsViewHolder, p1: Int) {
        p0.bind(films[p1].title, films[p1].rating, films[p1].posterUrlPrev, films[p1].year, films[p1].id.toString())
    }

}