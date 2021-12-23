package com.example.wickedtwister

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputLayout

class AddTransaction : Fragment() {

    private val args by navArgs<AddTransactionArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_add_transaction, container, false)
        val etAddTransactionDate = view.findViewById<TextInputLayout>(R.id.add_transaction_date)
        val etAddTransactionTag = view.findViewById<TextInputLayout>(R.id.add_transaction_tag)
        val etAddTransactionName = view.findViewById<TextInputLayout>(R.id.add_transaction_tra_name)
        val etAddTransactionValue = view.findViewById<TextInputLayout>(R.id.add_transaction_value)

        val tra = Transaction(accId = args.accountMain.accId,
            tagName = etAddTransactionTag.editText?.text.toString(),
            traValue = etAddTransactionValue.editText?.text.toString(),
            traName = etAddTransactionName.editText?.text.toString(),
            traDate = etAddTransactionDate.editText?.text.toString()
        )

        tra.create()

        if (tra.status){
            val action = AddTransactionDirections.actionAddTransactionToAccountMain(args.accountMain)
            findNavController().navigate(action)
        } else {
            Toast.makeText(this.context, "Deu ruim!", Toast.LENGTH_LONG).show()
        }

        return view
    }


}