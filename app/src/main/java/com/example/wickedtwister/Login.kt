package com.example.wickedtwister

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import java.util.concurrent.Executor

class Login : Fragment() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    lateinit var usr: String
    lateinit var pwd:String

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
        val et = view.findViewById<EditText>(R.id.login_user_pwd_et)

        fun View.hideSoftInput() {
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
        }

        fun btlog(auth: Boolean = false, usr:String = "", pwd:String = ""){
            view.hideSoftInput()

            if (auth) {
                val user = User(usrDoc = usr, usrPwd = pwd)
                user.login()
                if (user.status){
                    val action = LoginDirections.actionLoginToUserMain(user.usrId)
                    findNavController().navigate(action)
                }else{
                    txLogMsg.text = getString(R.string.wrong_SSN_or_PWD)
                }
            }else{
                if (etUsrDoc.editText?.text.toString() == "" ||
                    etUsrPwd.editText?.text.toString() == ""){
                    txLogMsg.text = getString(R.string.login_missing_credentials)
                }else{
                    val usr = User(usrDoc = etUsrDoc.editText?.text.toString(),
                        usrPwd = etUsrPwd.editText?.text.toString())
                    usr.login()
                    if (usr.status){
                        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
                        with (sharedPref.edit()) {
                            putString("username", etUsrDoc.editText?.text.toString())
                            apply()
                        }

                        with (sharedPref.edit()) {
                            putString("password", etUsrPwd.editText?.text.toString())
                            apply()
                        }

                        val action = LoginDirections.actionLoginToUserMain(usr.usrId)
                        findNavController().navigate(action)
                    }else{
                        txLogMsg.text = getString(R.string.wrong_SSN_or_PWD)
                    }
                }
            }
        }

        executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Log.v("Biometrics", "Error: $errString")
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Log.v("Biometrics", "Authentication succeeded!")
                    btlog(auth = true, usr = usr, pwd = pwd)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Log.v("Biometrics", "Authentication failed")
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        usr = sharedPref?.getString("username", "NaN").toString()
        pwd = sharedPref?.getString("password", "NaN").toString()

        if (usr != "NaN" && pwd != "NaN"){
            biometricPrompt.authenticate(promptInfo)
        }

        et.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                btlog()
            }
            true
        }
        btLogin.setOnClickListener {
            btlog()
        }

        btSignup.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_login_to_addUser)
        }

        return  view
    }
}