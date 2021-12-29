package com.example.wickedtwister

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

class UserMain : Fragment() {
    private val args by navArgs<UserMainArgs>()
    private lateinit var usr: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usr= User(usrId = args.userid)
        usr.get()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_main, container, false)

        val usrNameTV = view.findViewById<TextView>(R.id.user_main_user_nickname)
        usrNameTV.text = usr.usrNickname

        val pieChartTextView: TextView = view.findViewById(R.id.main_user_pie_chart_total_value)

        val pieChartSecondaryTextView: TextView = view.findViewById(R.id.main_user_pie_chart_budget_value)

        val pieChartTra:PieChart = view.findViewById(R.id.main_user_pie_chart)

        val pieChartBud:PieChart = view.findViewById(R.id.main_user_pie_chart_bud)

        val transactionRecyclerView:RecyclerView = view.findViewById(R.id.main_user_recycler_view)

        val settingsImageView: ImageView = view.findViewById(R.id.settings)

        val settingsLocation:TextView = view.findViewById(R.id.location)
        settingsLocation.text = "User Dashboard"

        settingsImageView.setOnClickListener {
            val action = UserMainDirections.actionUserMainToAddAccount(usr)
            findNavController().navigate(action)
        }

        pieChartTra.setOnClickListener {
            val action = UserMainDirections.actionUserMainToUserDetails(usr)
            findNavController().navigate(action)
        }

        usr.getAccounts()
        usr.serial()

        createPieChart(
            pieChartTextView = pieChartTextView,
            pieChartSecondaryTextView = pieChartSecondaryTextView,
            pieChartTra = pieChartTra,
            pieChartBud = pieChartBud,
            MapTra = usr.serialTra,
            MapBud = usr.serialBud)

        createRecyclerView(transactionRecyclerView, usr.transactions, usr.accounts)

        return view
    }

    @SuppressLint("SetTextI18n")
    fun createPieChart(pieChartTextView: TextView,
                       pieChartSecondaryTextView: TextView,
                       pieChartTra: PieChart,
                       pieChartBud: PieChart,
                       MapTra:MutableMap<String, Float>,
                       MapBud:MutableMap<String, Float>
    ){
        val formatter: ValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "" + value.toInt()
            }
        }

        val pieColorsBud = mutableListOf<Int>()
        val pieColorsTra  = mutableListOf<Int>()

        val colorsList = listOf(
            R.color.holo_green_light,
            R.color.holo_blue_light,
            R.color.holo_orange_light,
            R.color.holo_red_light,
            R.color.holo_purple,
            R.color.holo_blue_dark,
            R.color.holo_green_dark,
            R.color.holo_orange_dark,
            R.color.holo_red_dark,
            R.color.win8_blue,
            R.color.win8_brown,
            R.color.win8_green,
            R.color.win8_orange,
            R.color.teal,
            R.color.Orange,
            R.color.OrangeRed)

        val datasetBud = mutableListOf<PieEntry>()
        val datasetTra = mutableListOf<PieEntry>()

        var counter = 0

        for (tag in MapBud.keys){
            val traValue = MapTra[tag]
            val budValue = MapBud[tag]
            var traAdditionalValue = 0f
            var budAdditionalValue = 0f

            if (traValue!! > budValue!!){
                budAdditionalValue = traValue - budValue
            } else{
                traAdditionalValue = budValue - traValue
            }

            if (traValue!=0f) {
                datasetTra.add(PieEntry(traValue, tag))
                pieColorsTra.add(ContextCompat.getColor(requireContext(), colorsList[counter]))
            }

            if (budValue!=0f) {
                datasetBud.add(PieEntry(budValue, ""))
                pieColorsBud.add(ContextCompat.getColor(requireContext(), colorsList[counter]))
            }

            if (traAdditionalValue!=0f){
                datasetTra.add(PieEntry(traAdditionalValue, ""))
                pieColorsTra.add(ColorUtils.setAlphaComponent(ContextCompat.getColor(requireContext(), colorsList[counter]), 150))
            }

            if (budAdditionalValue!=0f){
                datasetBud.add(PieEntry(budAdditionalValue, ""))
                pieColorsBud.add(ColorUtils.setAlphaComponent(ContextCompat.getColor(requireContext(), colorsList[counter]), 150))
            }
            counter += 1
        }

        //TODO Name it better.
        pieChartTextView.text = "SEK ${MapTra.map { it.value }.sum()}"
        pieChartSecondaryTextView.text = "SEK ${MapBud.map { it.value }.sum()}"

        val pieDSTra = PieDataSet(datasetTra, "Pie")
        pieDSTra.colors = pieColorsTra

        // Value lines
        pieDSTra.valueLinePart1Length = 0.6f
        pieDSTra.valueLinePart2Length = 0.3f
        pieDSTra.valueLineWidth = 2f
        pieDSTra.valueLinePart1OffsetPercentage = 115f  // Line starts outside of chart
        pieDSTra.isUsingSliceColorAsValueLineColor = true

        val pieDTTra = PieData(pieDSTra)
        pieDTTra.setDrawValues(true)
        pieDTTra.setValueFormatter(PercentFormatter(pieChartTra))
        pieDTTra.setValueTextSize(15f)
        pieDTTra.setValueFormatter(formatter)

        pieChartTra.legend.isEnabled = false
        pieChartTra.description.isEnabled = false
        pieChartTra.animateXY(750, 750)
        pieChartTra.setTouchEnabled(false)
        pieChartTra.minAngleForSlices = 1f
        pieChartTra.holeRadius = 60f

        pieChartTra.data = pieDTTra
        pieChartTra.setHoleColor(ContextCompat.getColor(requireContext(), R.color.black))
        pieChartTra.invalidate()

        val pieDSBud = PieDataSet(datasetBud, "Pie")
        pieDSBud.colors = pieColorsBud

        // Value lines
        pieDSBud.valueLinePart1Length = 0.6f
        pieDSBud.valueLinePart2Length = 0.3f
        pieDSBud.valueLineWidth = 2f
        pieDSBud.valueLinePart1OffsetPercentage = 115f  // Line starts outside of chart
        pieDSBud.isUsingSliceColorAsValueLineColor = true

        val pieDTBud = PieData(pieDSBud)
        pieDTBud.setDrawValues(false)
        pieDTBud.setValueFormatter(PercentFormatter(pieChartBud))
        pieDTBud.setValueTextSize(15f)
        pieDTBud.setValueFormatter(formatter)

        pieChartBud.legend.isEnabled = false
        pieChartBud.description.isEnabled = false
        pieChartBud.animateXY(750, 750)
        pieChartBud.setTouchEnabled(false)
        pieChartBud.minAngleForSlices = 1f
        pieChartBud.holeRadius = 90f

        pieChartBud.data = pieDTBud
        pieChartBud.setHoleColor(ContextCompat.getColor(requireContext(), R.color.black))
        pieChartBud.invalidate()

    }

    private fun createRecyclerView(transactionRV: RecyclerView,
                                   Transactions: List<Transaction>,
                                   accounts: MutableMap<String, Account>){
        transactionRV.layoutManager = LinearLayoutManager(activity)
        transactionRV.adapter = TransactionAdapter(Transactions, accounts)
    }
}