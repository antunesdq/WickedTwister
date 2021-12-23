package com.example.wickedtwister

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wickedtwister.R
import com.example.wickedtwister.AccountRViewAdaptor


class UserDetails : Fragment() {
    private val args by navArgs<UserDetailsArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_details, container, false)
        val usr = args.userMain
        val settingsIV = view.findViewById<ImageView>(R.id.settings)
        settingsIV.setOnClickListener {
            val action = UserDetailsDirections.actionUserDetailsToAddAccount(usr)
            findNavController().navigate(action)
        }
        usr.getAccounts()
        val rview = view.findViewById<RecyclerView>(R.id.account_recycler_view)
        rview.layoutManager = LinearLayoutManager(context)
        rview.adapter = AccountRViewAdaptor(dataset = usr.accounts){
            val action = UserDetailsDirections.actionUserDetailsToAccountMain(accountMain = it)
            findNavController().navigate(action)
        }
        return view
    }
}