package com.example.aupairconnect.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.aupairconnect.R

class AccountFragment : Fragment() {

    private lateinit var logoutButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        logoutButton = view.findViewById(R.id.btnLogOut)

        logoutButton.setOnClickListener {
            activity?.finish()
        }

        return view
    }


}