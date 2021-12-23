package com.example.wickedtwister

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs


class menu : Fragment() {
    //private val args by navArgs<menuArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_menu, container, false)
        val btAddTransaction = view.findViewById<Button>(R.id.menu_add_transaction_button)
        val btAddAccount = view.findViewById<Button>(R.id.menu_add_account_button)

       // btAddAccount.setOnClickListener {
         //   val action = menuDirections.actionMenuToAddAccount(args.userMain)
           // findNavController().navigate(action)
        //}

//        btAddTransaction.setOnClickListener {
  //          val action = menuDirections.actionMenuToAddTransaction(args.userMain, args.accountMain)
    //        findNavController().navigate(action)
      //  }

        return view
    }


}