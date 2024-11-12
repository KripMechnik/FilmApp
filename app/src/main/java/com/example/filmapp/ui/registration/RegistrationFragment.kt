package com.example.filmapp.ui.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.filmapp.R
import com.example.filmapp.databinding.FragmentListBinding
import com.example.filmapp.databinding.FragmentRegistrationBinding
import com.example.filmapp.ui.firebase.UserData
import com.example.filmapp.ui.list.ListViewModel
import com.example.filmapp.ui.profile.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var viewModel: RegistrationViewModel
    private lateinit var emailET: EditText
    private lateinit var loginET: EditText
    private lateinit var passwordET: EditText
    private lateinit var buttonRegister: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[RegistrationViewModel::class.java]
        binding = FragmentRegistrationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            emailET = email
            loginET = login
            passwordET = password
            buttonRegister = toRegister
        }
        buttonRegister.setOnClickListener {
            val user = UserData(
                email = emailET.text.toString(),
                password = passwordET.text.toString(),
                username = loginET.text.toString(),
                photoUrl = "")
            viewModel.registerUser(user)
        }

        viewModel.registrationResult.observe(viewLifecycleOwner){
            if (it){
                Toast.makeText(requireContext(), "Регистрация прошла успешно!", Toast.LENGTH_LONG).show()
                parentFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                    .replace(R.id.container, ProfileFragment())
                    .commit()
            }
        }


    }
}