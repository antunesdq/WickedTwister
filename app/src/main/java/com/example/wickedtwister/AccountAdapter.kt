package com.example.wickedtwister

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlin.math.abs

class AccountAdapter(private val dataset: List<Account>, val context: Context):
    RecyclerView.Adapter<AccountAdapter.MyViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    class MyViewHolder(view: View, listener: onItemClickListener) : RecyclerView.ViewHolder(view) {
        val pieChartTra: PieChart = view.findViewById(R.id.account_card_pie_chart)
        val pieChartBud: PieChart = view.findViewById(R.id.account_card_pie_chart_bud)
        val transactionRv: RecyclerView = view.findViewById(R.id.account_card_recycler_view)
        val accountsAlias: TextView = view.findViewById(R.id.account_card_acc_alias)
        val TraTw:TextView = view.findViewById(R.id.account_card_pie_chart_total_value)
        val BudTw:TextView = view.findViewById(R.id.account_card_pie_chart_budget_value)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(position = absoluteAdapterPosition)
            }
        }

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.account_card, viewGroup, false)

        return MyViewHolder(view, mListener)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {

        val account = dataset[position]

        account.serial()

        createPieChart(viewHolder.TraTw,
            viewHolder.BudTw,
            viewHolder.pieChartTra,
            viewHolder.pieChartBud,
            account.serialTra,
            account.serialBud)

        val accounts = mutableMapOf<String, Account>()

        accounts[account.accId] = account


        viewHolder.transactionRv.layoutManager = LinearLayoutManager(context)
        viewHolder.transactionRv.adapter = TransactionAdapter(account.transactions, accounts)
        viewHolder.accountsAlias.text = account.accAlias
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataset.size
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
            R.color.holo_red_dark)

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
                pieColorsTra.add(ContextCompat.getColor(context, colorsList[counter]))
            }

            if (budValue!=0f) {
                datasetBud.add(PieEntry(budValue, ""))
                pieColorsBud.add(ContextCompat.getColor(context, colorsList[counter]))
            }

            if (traAdditionalValue!=0f){
                datasetTra.add(PieEntry(traAdditionalValue, ""))
                pieColorsTra.add(ColorUtils.setAlphaComponent(ContextCompat.getColor(context, colorsList[counter]), 150))
            }

            if (budAdditionalValue!=0f){
                datasetBud.add(PieEntry(budAdditionalValue, ""))
                pieColorsBud.add(ColorUtils.setAlphaComponent(ContextCompat.getColor(context, colorsList[counter]), 150))
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
        pieChartTra.setHoleColor(ContextCompat.getColor(context, R.color.black))
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
        pieChartBud.setHoleColor(ContextCompat.getColor(context, R.color.black))
        pieChartBud.invalidate()

    }
}

