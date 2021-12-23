package com.example.wickedtwister

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout

class Login : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val btLogin = view.findViewById<Button>(R.id.login_signin_button)
        val btSignup = view.findViewById<Button>(R.id.login_signup_button)
        val etUsrDoc = view.findViewById<TextInputLayout>(R.id.login_user_doc)
        val etUsrPwd = view.findViewById<TextInputLayout>(R.id.login_user_pwd)
        val txLogMsg = view.findViewById<TextView>(R.id.login_message)

        btLogin.setOnClickListener {
            if (etUsrDoc.editText?.text.toString() == "" ||
                etUsrPwd.editText?.text.toString() == ""){
                txLogMsg.text = getString(R.string.login_missing_credentials)
            }else{
                val usr = User(usrDoc = etUsrDoc.editText?.text.toString(),
                    usrPwd = etUsrPwd.editText?.text.toString())
                usr.login()
                if (usr.status){
                    val action = LoginDirections.actionLoginToUserMain(usr.usrId)
                    findNavController().navigate(action)
                }else{
                    txLogMsg.text = getString(R.string.wrong_SSN_or_PWD)
                }
            }
        }

        btSignup.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_login_to_addUser)
        }

        return  view
    }
}