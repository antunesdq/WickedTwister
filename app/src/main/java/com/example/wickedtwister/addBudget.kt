package com.example.wickedtwister

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputLayout
import java.time.LocalDateTime
import java.util.*

class addBudget : Fragment() {

    private val args by navArgs<AddTransactionArgs>()
    private lateinit var usr: User
    private lateinit var acc: Account

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_add_budget, container, false)
        val etAddBudgetTag = view.findViewById<TextInputLayout>(R.id.add_budget_tag)
        val etAddBudgetValue = view.findViewById<TextInputLayout>(R.id.add_budget_value)
        val btAddTransaction = view.findViewById<Button>(R.id.add_budget_button)


        btAddTransaction.setOnClickListener {

            val bud = Budget(accId = args.accountMain.accId,
                tagName = etAddBudgetTag.editText?.text.toString(),
                budValue = etAddBudgetValue.editText?.text.toString().toFloat(),
            )
            bud.create()
            acc = args.accountMain
            usr = args.userMain
            if (bud.status){
                val action = addBudgetDirections.actionAddBudgetToAccountMain(accountMain = acc.accId, userMain = usr)
                findNavController().navigate(action)
            } else {
                Toast.makeText(this.context, "Deu ruim!", Toast.LENGTH_LONG).show()
            }


        }
        return view
    }
}