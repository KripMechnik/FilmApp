package com.example.filmapp.ui.recommendations

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.filmapp.R
import com.example.filmapp.databinding.FragmentRecommendationsBinding
import com.example.filmapp.databinding.RecommendationsItemBinding
import com.example.filmapp.ui.detailed.DetailFragment
import com.example.filmapp.ui.detailed.DetailViewModel
import com.example.filmapp.ui.recommendations.adapter.RecommendationsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecommendationsFragment : Fragment() {
    private lateinit var binding: FragmentRecommendationsBinding
    private lateinit var viewModel: RecommendationsViewModel
    private lateinit var detailViewModel: DetailViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecommendationsBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[RecommendationsViewModel::class.java]
        detailViewModel = ViewModelProvider(requireActivity())[DetailViewModel::class.java]
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchView.apply {
            setIconifiedByDefault(false)
            queryHint = getString(R.string.search)
        }

        viewModel.state.observe(viewLifecycleOwner){
            val adapter = RecommendationsAdapter(it){id ->
                detailViewModel.id.value = id
                detailViewModel.getFilm()
                val fragmentTransaction = parentFragmentManager.beginTransaction()
                fragmentTransaction.setReorderingAllowed(true).setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                fragmentTransaction.replace(R.id.container, DetailFragment())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
            binding.recommendationsRecycler.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }
}