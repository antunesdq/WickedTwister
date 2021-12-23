package com.example.wickedtwister

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout

class AddUser : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_add_user, container, false)

        val btAddUser = view.findViewById<Button>(R.id.add_user_button)
        val etAddUserDocument = view.findViewById<TextInputLayout>(R.id.add_user_doc)
        val etAddUserPassword = view.findViewById<TextInputLayout>(R.id.add_user_pwd)
        val etAddUserEmail = view.findViewById<TextInputLayout>(R.id.add_user_email)
        val etAddUserNickname = view.findViewById<TextInputLayout>(R.id.add_user_nickname)
        // TODO Add message of error
        btAddUser.setOnClickListener {
            if (etAddUserDocument.editText?.text.toString() == "" || etAddUserPassword.editText?.text.toString() == ""
                || etAddUserEmail.editText?.text.toString() == "" || etAddUserNickname.editText?.text.toString() == ""
            ) {
                // put it here
            } else {
                val usr = User(usrDoc = etAddUserDocument.editText?.text.toString(),
                    usrPwd = etAddUserPassword.editText?.text.toString(),
                    usrEmail = etAddUserEmail.editText?.text.toString(),
                    usrNickname = etAddUserNickname.editText?.text.toString()
                )
                usr.create()
                usr.login()
                if (usr.status) {
                    val action = AddUserDirections.actionAddUserToUserMain(usr.usrId)
                    findNavController().navigate(action)
                } else {
                    Toast.makeText(this.context, "Deu ruim!", Toast.LENGTH_LONG).show()
                }

            }
        }
        return view
    }
}