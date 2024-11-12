package com.example.filmapp.ui.all_users.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.filmapp.R
import com.example.filmapp.databinding.FilmItemBinding
import com.example.filmapp.databinding.UserItemBinding
import com.example.filmapp.ui.common.UserState

class AllUsersAdapter(
    private val users: List<UserState>,
    private val onClick: (uid: String) -> Unit,
) : RecyclerView.Adapter<AllUsersAdapter.AllUsersViewHolder>() {
    inner class AllUsersViewHolder(private val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(username: String, photoUrl: String, uid: String){
            binding.apply {
                buttonAdd.setOnClickListener { onClick(uid) }
                login.text = username
                Glide.with(photo).load(photoUrl).error(R.drawable.user_icon).apply(RequestOptions().centerCrop()).into(photo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllUsersViewHolder {
        return AllUsersViewHolder(
            UserItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: AllUsersViewHolder, position: Int) {
        holder.bind(users[position].username, users[position].photoUrl, users[position].uid)
    }
}