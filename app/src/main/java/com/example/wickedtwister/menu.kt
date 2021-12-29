package com.example.wickedtwister

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import java.time.LocalDateTime


class menu : Fragment() {
    private val args by navArgs<AddTransactionArgs>()
    private lateinit var usr: User
    private lateinit var acc: Account
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)
        val btMenuAddTransaction = view.findViewById<Button>(R.id.menu_add_transaction_button)
        val btMenuAddBudget = view.findViewById<Button>(R.id.menu_add_budget_button)

        btMenuAddTransaction.setOnClickListener {
            acc = args.accountMain
            usr = args.userMain
            val action = menuDirections.actionMenu2ToAddTransaction(accountMain = acc, userMain = usr)
            findNavController().navigate(action)
        }

        btMenuAddBudget.setOnClickListener {
            acc = args.accountMain
            usr = args.userMain
            val action = menuDirections.actionMenu2ToAddBudget(accountMain = acc, userMain = usr)
            findNavController().navigate(action)
        }

        return view
    }

}