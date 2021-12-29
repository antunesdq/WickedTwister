package com.example.wickedtwister

import android.widget.TextView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import org.json.JSONObject


fun jsonToMap(json: JSONObject): MutableMap<String, String> {
        val map: MutableMap<String, String> = linkedMapOf()
        for (key in json.keys()) {
            map[key] = json[key].toString()
        }
        return map
    }


class Utils {
    //val ipaws = "54.72.218.49"
    val ipaws = "10.0.2.2"
}