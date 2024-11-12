package com.example.filmapp.ui.friends.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.filmapp.R
import com.example.filmapp.databinding.FriendItemBinding
import com.example.filmapp.databinding.UserItemBinding
import com.example.filmapp.ui.common.UserState

class FriendsAdapter (
    private val friends: List<UserState>
) : RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder>() {

    inner class FriendsViewHolder(private val binding: FriendItemBinding) : ViewHolder(binding.root){
        fun bind(username: String, photoUrl: String){
            binding.apply {
                login.text = username
                Glide.with(photo).load(photoUrl).error(R.drawable.user_icon).apply(RequestOptions().centerCrop()).into(photo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        return FriendsViewHolder(
            FriendItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = friends.size

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        holder.bind(friends[position].username, friends[position].photoUrl)
    }
}