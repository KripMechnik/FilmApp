package com.example.filmapp.ui.detailed.bottom_sheet_dialog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.filmapp.R
import com.example.filmapp.databinding.FriendItemBinding
import com.example.filmapp.ui.common.UserState
import com.example.filmapp.ui.detailed.bottom_sheet_dialog.FriendsSheetDialog

class FriendsSheetAdapter (
    private val friends: List<UserState>,
    private val onClick: (uidTo: String, id: String) -> Unit,
    private val id: String,
    private val dialog: FriendsSheetDialog
) : RecyclerView.Adapter<FriendsSheetAdapter.FriendsViewHolder>() {

    inner class FriendsViewHolder(private val binding: FriendItemBinding) : ViewHolder(binding.root){
        fun bind(username: String, photoUrl: String, uid: String){
            binding.apply {
                login.text = username
                Glide.with(photo).load(photoUrl).error(R.drawable.user_icon).apply(RequestOptions().centerCrop()).into(photo)
                root.setOnClickListener {
                    onClick(uid, id)
                    dialog.dismiss()
                }
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
        holder.bind(friends[position].username, friends[position].photoUrl, friends[position].uid)
    }
}