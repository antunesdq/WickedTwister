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
import com.google.android.material.textfield.TextInputLayout

class AddAccount : Fragment() {

    private val args by navArgs<AddAccountArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_account, container, false)
        val createAccountbtn = view.findViewById<Button>(R.id.add_account_button)
        createAccountbtn.setOnClickListener {
            val etAddAccountAlias = view.findViewById<TextInputLayout>(R.id.add_account_alias)
            val etAddAccountTag = view.findViewById<TextInputLayout>(R.id.add_account_tag)
            val etAddAccountRefDay = view.findViewById<TextInputLayout>(R.id.add_account_refday)

            val acc = Account(usrId = args.userMain.usrId,
                accAlias = etAddAccountAlias.editText?.text.toString(),
                tagName = etAddAccountTag.editText?.text.toString(),
                accRefDay = etAddAccountRefDay.editText?.text.toString().toInt()
            )

            acc.create()
            if (acc.status){
                val usr = args.userMain
                val action = AddAccountDirections.actionAddAccountToUserDetails(usr)
                findNavController().navigate(action)
            } else {
                Toast.makeText(this.context, "Deu ruim!", Toast.LENGTH_LONG).show()
            }

        }
        return view
    }

}