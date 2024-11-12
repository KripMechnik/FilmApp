package com.example.filmapp.ui.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.filmapp.R
import com.example.filmapp.databinding.FragmentProfileBinding
import com.example.filmapp.ui.all_users.AllUsersFragment
import com.example.filmapp.ui.friends.FriendsFragment
import com.example.filmapp.ui.recommendations.RecommendationsFragment
import com.google.android.material.imageview.ShapeableImageView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileOutputStream

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var photo: ImageView
    private lateinit var username: TextView
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var friendsBtn: ImageButton
    private lateinit var recommendationsBtn: ImageButton
    private lateinit var addFriendBtn: ImageButton
    private lateinit var logoutButton: ShapeableImageView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val inputStream = requireActivity().contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(inputStream).also {
                        inputStream?.close()
                    }
                    withContext(Dispatchers.Main) {
                        photo.setImageBitmap(bitmap)
                    }
                    launch {
                        saveUriToFile(bitmap, requireContext())
                    }

                }


            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            photo = profileImage
            username = profileName
            friendsBtn = friendsButton
            recommendationsBtn = recommendationsButton
            addFriendBtn = peopleButton
            logoutButton = logout
        }

        photo.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        }
        lifecycleScope.launch(Dispatchers.IO) {
            loadUriFromFile(requireContext())?.let {
                withContext(Dispatchers.Main) {
                    photo.setImageBitmap(it)
                }
            }
        }

        logoutButton.setOnClickListener {
            viewModel.logout()
            parentFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.container, parentFragmentManager.findFragmentByTag("ListFragment")!!)
                .commit()
        }

        friendsBtn.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.container, FriendsFragment())
                .addToBackStack(null)
                .commit()
        }

        addFriendBtn.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.container, AllUsersFragment())
                .addToBackStack(null)
                .commit()
        }

        recommendationsBtn.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.container, RecommendationsFragment())
                .addToBackStack(null)
                .commit()
        }

        viewModel.state.observe(viewLifecycleOwner){
            username.text = it.username
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            parentFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.container, parentFragmentManager.findFragmentByTag("ListFragment")!!)
                .commit()
        }
    }

    fun saveUriToFile(bitmap: Bitmap, context: Context) {
        val filename = "my_bitmap_image.png"
        val fileOutputStream: FileOutputStream
        try {
            fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream) // 100 – качество для PNG
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadUriFromFile(context: Context): Bitmap? {
        val filename = "my_bitmap_image.png"

        return try {
            val fileInputStream = context.openFileInput(filename)
            BitmapFactory.decodeStream(fileInputStream).also {
                fileInputStream.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }


    }
}