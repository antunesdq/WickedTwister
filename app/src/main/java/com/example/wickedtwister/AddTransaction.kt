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
import java.time.format.DateTimeFormatter
import android.app.DatePickerDialog
import java.time.LocalDateTime
import java.util.*


class AddTransaction : Fragment() {

    private val args by navArgs<AddTransactionArgs>()
    private lateinit var usr: User
    private lateinit var acc: Account

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_add_transaction, container, false)
        val btAddTransactionDate = view.findViewById<Button>(R.id.add_transaction_date)
        val etAddTransactionTag = view.findViewById<TextInputLayout>(R.id.add_transaction_tag)
        val etAddTransactionName = view.findViewById<TextInputLayout>(R.id.add_transaction_tra_name)
        val etAddTransactionValue = view.findViewById<TextInputLayout>(R.id.add_transaction_value)
        val btAddTransaction = view.findViewById<Button>(R.id.add_transaction_button)

        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        var newDay = 1
        var newMonth = 1
        var newYear = 1
        btAddTransactionDate.setOnClickListener {
            val dpd = DatePickerDialog(requireContext(), { view, myear, mmonth, mday ->
                btAddTransactionDate.text = "${myear}/${mmonth}/${mday}"
                newDay = mday
                newMonth = mmonth
                newYear = myear
            }, year,month,day)
            dpd.show()
        }

        btAddTransaction.setOnClickListener {

            val tra = Transaction(
                accId = args.accountMain.accId,
                tagName = etAddTransactionTag.editText?.text.toString(),
                traValue = etAddTransactionValue.editText?.text.toString().toFloat(),
                traName = etAddTransactionName.editText?.text.toString(),
//                traDate = Date.valueOf(LocalDate.parse("${newYear}-${newMonth}-${newDay}T17:11:00Z", format).toString(),)
                traDate = LocalDateTime.of(newYear,newMonth,newDay,0,0,0,0)
            )
            tra.create()
            acc = args.accountMain
            usr = args.userMain
            if (tra.status){
                val action = AddTransactionDirections.actionAddTransactionToAccountMain(accountMain = acc.accId, userMain = usr)
                findNavController().navigate(action)
            } else {
                Toast.makeText(this.context, "Deu ruim!", Toast.LENGTH_LONG).show()
            }


        }
        return view
    }
}