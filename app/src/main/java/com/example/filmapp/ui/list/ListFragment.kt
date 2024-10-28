package com.example.filmapp.ui.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.filmapp.R
import com.example.filmapp.databinding.FragmentListBinding
import com.example.filmapp.ui.detailed.DetailViewModel
import com.example.filmapp.ui.list.adapter.FilmListAdapter
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.search.SearchBar


import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var viewModel: ListViewModel
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var recycler: RecyclerView
    private lateinit var profileButton: ShapeableImageView
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[ListViewModel::class.java]
        detailViewModel = ViewModelProvider(requireActivity())[DetailViewModel::class.java]
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler = binding.filmsRecycler

        searchView = binding.searchView
        searchView.apply {
            setIconifiedByDefault(false)
            queryHint = getString(R.string.search)
        }

        profileButton = binding.profileButton

        profileButton.setOnClickListener {
            
        }

        viewModel.state.observe(viewLifecycleOwner, Observer {
            if(it.error.isNotEmpty()){
                Log.e("RRR", it.error)
            }
            val adapter = FilmListAdapter(it.films, detailViewModel, requireActivity())

            recycler.adapter = adapter
            adapter.notifyDataSetChanged()

        })
    }
}