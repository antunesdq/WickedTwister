package com.example.wickedtwister

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
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

        val pieColors = mutableListOf<Int>()
        val colorsList = listOf<Int>(
            R.color.holo_green_light,
            R.color.holo_blue_light,
            R.color.holo_orange_light,
            R.color.holo_red_light,
            R.color.holo_purple,
            R.color.holo_blue_dark,
            R.color.holo_green_dark,
            R.color.holo_orange_dark,
            R.color.holo_red_dark)

        val datasetBud = mutableListOf<PieEntry>()
        val datasetTra = mutableListOf<PieEntry>()

        for (tag in MapBud.keys){
            var C = 0f
            var D = 0f
            if (MapTra[tag]!! > MapBud[tag]!!){
                D = MapTra[tag]!! - MapBud[tag]!!
            } else{
                C = MapBud[tag]!! - MapTra[tag]!!
            }
            datasetBud.add(PieEntry(MapBud[tag]!!, ""))
            datasetBud.add(PieEntry(D, ""))
            datasetTra.add(PieEntry(MapTra[tag]!!, tag))
            datasetTra.add(PieEntry(C, ""))
        }

        //TODO Name it better.
        pieChartTextView.text = "SEK ${MapTra.map { it.value }.sum()}"
        pieChartSecondaryTextView.text = "SEK ${MapBud.map { it.value }.sum()}"

        //TODO Make it so it takes the color from the tag.
        for (i in 0..(datasetTra.size/2)){
            pieColors.add(ContextCompat.getColor(requireContext(), colorsList[i]))
            pieColors.add(ContextCompat.getColor(requireContext(), R.color.gabigrey))
        }

        val pieDSTra = PieDataSet(datasetTra, "Pie")
        pieDSTra.colors = pieColors

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
        pieDSBud.colors = pieColors

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

    fun createRecyclerView(transactionRV: RecyclerView,
                           Transactions: List<Transaction>,
                           accounts: MutableMap<String, Account>){
        transactionRV.layoutManager = LinearLayoutManager(activity)
        transactionRV.adapter = TransactionAdapter(Transactions, accounts)
    }

}