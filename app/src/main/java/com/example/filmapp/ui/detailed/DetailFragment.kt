package com.example.filmapp.ui.detailed

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.filmapp.R
import com.example.filmapp.databinding.FragmentDetailBinding
import com.example.filmapp.ui.detailed.bottom_sheet_dialog.FriendsSheetDialog
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var toolbar: MaterialToolbar
    private lateinit var viewModel: DetailViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[DetailViewModel::class.java]
        binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar = binding.toolbar
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
        if ((activity as AppCompatActivity?)?.supportActionBar != null){
            (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        toolbar.setNavigationOnClickListener {
            (requireActivity() as AppCompatActivity).supportFragmentManager.popBackStack()
        }

        viewModel.state.observe(viewLifecycleOwner) {
            binding.tvShortDesc.text = it.film?.shortDescription ?: ""
            binding.tvDescription.text = it.film?.description ?: ""
            binding.ratingBar.rating = (it.film?.rating?.div(2) ?: 0.0).toFloat()
            binding.toolbar.title = it.film?.title ?: ""
            Glide.with(binding.ivPoster).load(it.film?.posterUrl).error(Color.Gray)
                .apply(RequestOptions().centerCrop()).into(binding.ivPoster)
        }

        binding.sendButton.setOnClickListener {
            val fragment = FriendsSheetDialog()
            val bundle = Bundle()
            bundle.putString("id", viewModel.id.value)
            fragment.arguments = bundle
            fragment.show(parentFragmentManager, null)
        }

    }

}