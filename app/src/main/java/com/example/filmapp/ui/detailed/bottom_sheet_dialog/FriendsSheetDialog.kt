package com.example.filmapp.ui.detailed.bottom_sheet_dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.filmapp.databinding.FriendsSheetBinding
import com.example.filmapp.ui.detailed.bottom_sheet_dialog.adapter.FriendsSheetAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendsSheetDialog : BottomSheetDialogFragment() {

    private lateinit var binding: FriendsSheetBinding
    private lateinit var viewModel: FriendsSheetViewModel
    private lateinit var id: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        id = arguments?.getString("id", "") ?: ""
        viewModel = ViewModelProvider(this)[FriendsSheetViewModel::class.java]
        binding = FriendsSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner){
            val adapter = FriendsSheetAdapter(it, viewModel::sendRecommendation, id, this)
            binding.friendsRecycler.adapter = adapter
            adapter.notifyDataSetChanged()
        }

    }

}