package com.example.wickedtwister

import android.content.Context
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


class UserDetails : Fragment() {
    private val args by navArgs<UserDetailsArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_details, container, false)
        val usr = args.userMain

        val settingsIV = view.findViewById<ImageView>(R.id.settings)

        settingsIV.setOnClickListener {
            val action = UserDetailsDirections.actionUserDetailsToAddAccount(usr)
            findNavController().navigate(action)
        }
        val accountsRview = view.findViewById<RecyclerView>(R.id.detail_user_recycler_view)
        val accs = fazai(usr)
        createAccountRecyclerView(accountsRview, accs, requireContext())

        return view
    }
    fun createAccountRecyclerView(accountsRview: RecyclerView, Accounts:List<Account>, context:Context){
        accountsRview.layoutManager = LinearLayoutManager(activity)
        accountsRview.adapter = AccountAdapter(Accounts, context)
    }
    fun fazai(user:User): MutableList<Account> {
        user.getAccounts()


        val accounts: MutableList<Account> = mutableListOf()
        for (acc in user.accounts.values){
            accounts.add(acc)
        }
        return accounts
    }
}