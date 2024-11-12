package com.example.filmapp.ui.list


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.filmapp.R
import com.example.filmapp.databinding.FragmentListBinding
import com.example.filmapp.ui.detailed.DetailViewModel
import com.example.filmapp.ui.list.adapter.FilmListAdapter
import com.example.filmapp.ui.profile.ProfileFragment
import com.example.filmapp.ui.sign_in.SignInFragment
import com.google.android.material.imageview.ShapeableImageView
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
    ): View {
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

        viewModel.photoUrl.observe(viewLifecycleOwner){
            if (it.isNotBlank()){
                Glide.with(profileButton).load(it).error(R.drawable.user_icon).apply(RequestOptions().centerCrop()).into(profileButton)
            }
        }




        profileButton.setOnClickListener {
            if (viewModel.checkAuth()){
                parentFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                    .replace(R.id.container, ProfileFragment())
                    .addToBackStack("ListFragment")
                    .commit()
            } else {
                parentFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                    .replace(R.id.container, SignInFragment())
                    .addToBackStack("ListFragment")
                    .commit()
            }
        }

        viewModel.state.observe(viewLifecycleOwner) {
            if (it.error.isNotEmpty()) {
                Log.e("RRR", it.error)
            }
            val adapter = FilmListAdapter(it.films, detailViewModel, requireActivity())

            recycler.adapter = adapter
            adapter.notifyDataSetChanged()

        }
    }
}