package com.example.filmapp.ui.all_users

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.filmapp.R
import com.example.filmapp.databinding.FragmentAllUsersBinding
import com.example.filmapp.ui.all_users.adapter.AllUsersAdapter
import com.google.android.material.imageview.ShapeableImageView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllUsersFragment : Fragment() {

    private lateinit var binding: FragmentAllUsersBinding
    private lateinit var viewModel: AllUsersViewModel
    private lateinit var recycler: RecyclerView
    private lateinit var profileButton: ShapeableImageView
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[AllUsersViewModel::class.java]
        binding = FragmentAllUsersBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            recycler = allUsersRecycler
            this@AllUsersFragment.profileButton = profileButton
            this@AllUsersFragment.searchView = searchView.apply {
                setIconifiedByDefault(false)
                queryHint = getString(R.string.search)
            }
        }




        viewModel.state.observe(viewLifecycleOwner){
            Log.i("users", it.toString())
            val adapter = AllUsersAdapter(viewModel.state.value ?: emptyList(), viewModel::sendInvite)
            recycler.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }
}