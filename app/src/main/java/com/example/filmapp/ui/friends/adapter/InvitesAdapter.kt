package com.example.filmapp.ui.friends.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.filmapp.R
import com.example.filmapp.databinding.FriendItemBinding
import com.example.filmapp.databinding.WaitingFriendItemBinding
import com.example.filmapp.ui.common.UserState

class InvitesAdapter(
    private val invites: List<UserState>,
    private val onAccept: (uid: String) -> Unit,
    private val onDecline: (uid: String) -> Unit
) : RecyclerView.Adapter<InvitesAdapter.InvitesViewHolder>() {

    inner class InvitesViewHolder(private val binding: WaitingFriendItemBinding) : ViewHolder(binding.root){
        fun bind(username: String, photoUrl: String, uid: String){
            binding.apply {
                login.text = username
                Glide.with(photo).load(photoUrl).error(R.drawable.user_icon).apply(RequestOptions().centerCrop()).into(photo)
                buttonAdd.setOnClickListener { onAccept(uid) }
                buttonReject.setOnClickListener { onDecline(uid) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvitesViewHolder {
        return InvitesViewHolder(
            WaitingFriendItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = invites.size

    override fun onBindViewHolder(holder: InvitesViewHolder, position: Int) {
        holder.bind(invites[position].username, invites[position].photoUrl, invites[position].uid)
    }
}