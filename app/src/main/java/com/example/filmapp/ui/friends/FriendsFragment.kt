package com.example.filmapp.ui.friends

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.filmapp.R
import com.example.filmapp.databinding.FragmentFriendsBinding
import com.example.filmapp.databinding.FragmentListBinding
import com.example.filmapp.ui.friends.adapter.FriendsAdapter
import com.example.filmapp.ui.friends.adapter.InvitesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendsFragment : Fragment() {

    private lateinit var binding: FragmentFriendsBinding
    private lateinit var viewModel: FriendsViewModel
    private lateinit var friendsTV: TextView
    private lateinit var waitingTV: TextView
    private lateinit var friendsRecycler: RecyclerView
    private lateinit var invitesRecycler: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[FriendsViewModel::class.java]
        binding = FragmentFriendsBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchView.apply {
            setIconifiedByDefault(false)
            queryHint = getString(R.string.search)
        }

        binding.apply {
            friendsTV = friendsTextView
            waitingTV = waitingTextView
            this@FriendsFragment.friendsRecycler = friendsRecycler
            invitesRecycler = waitingFriendsRecycler
        }

        viewModel.friendsState.observe(viewLifecycleOwner){
            if (it.isNotEmpty()){
                friendsTV.visibility = View.VISIBLE
                val adapter = FriendsAdapter(it)
                friendsRecycler.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        }

        viewModel.notAcceptedFriendsState.observe(viewLifecycleOwner){
            if (it.isNotEmpty()){
                waitingTV.visibility = View.VISIBLE
                val adapter = InvitesAdapter(it, viewModel::acceptFriend, viewModel::declineFriend)
                invitesRecycler.adapter = adapter
                adapter.notifyDataSetChanged()
            }

        }
    }
}