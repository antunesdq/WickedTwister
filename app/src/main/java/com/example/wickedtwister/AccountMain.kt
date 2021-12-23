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


class AccountMain : Fragment() {

    private val args by navArgs<AccountMainArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account_main, container, false)
        val acc = args.accountMain
        val settingsIV = view.findViewById<ImageView>(R.id.settings)
        settingsIV.setOnClickListener {
            val action = AccountMainDirections.actionAccountMainToAddTransaction(args.accountMain)
            findNavController().navigate(action)
        }
        val rview = view.findViewById<RecyclerView>(R.id.transaction_recycler_view)
        rview.layoutManager = LinearLayoutManager(context)
        rview.adapter = TransactionRViewAdaptor(dataset = acc.transactions)
        return view
    }


}