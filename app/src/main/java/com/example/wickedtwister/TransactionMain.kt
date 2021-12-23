package com.example.wickedtwister

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.navArgs

class TransactionMain : Fragment() {
    private val args by navArgs<TransactionMainArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_transaction_main, container, false)
        val settingsIV = view.findViewById<ImageView>(R.id.settings)
        //settingsIV.setOnClickListener {
          //  val action = TransactionMainDirections.actionTransactionMainToMenu(args.userMain, args.accountMain)
            //findNavController().navigate(action)
        //}
        return view
    }


}