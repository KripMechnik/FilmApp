package com.example.filmapp.ui.sign_in

import android.os.Bundle
import android.util.Log
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
import com.example.filmapp.databinding.FragmentSignInBinding
import com.example.filmapp.ui.profile.ProfileFragment
import com.example.filmapp.ui.registration.RegistrationFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private lateinit var viewModel: SignInViewModel
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var buttonSignIn: Button
    private lateinit var buttonToRegister: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[SignInViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            etEmail = email
            etPassword = password
            buttonSignIn = signInButton
            buttonToRegister = toRegisterButton
        }

        buttonSignIn.setOnClickListener {
            viewModel.signIn(etEmail.text.toString(), etPassword.text.toString())
        }

        viewModel.result.observe(viewLifecycleOwner){
            if (it){
                parentFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                    .replace(R.id.container, ProfileFragment())
                    .commit()
            } else {
                Toast.makeText(requireContext(), "Вход в аккаунт не был выполнен", Toast.LENGTH_LONG).show()
            }
        }

        buttonToRegister.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.container, RegistrationFragment())
                .commit()
        }
    }
}