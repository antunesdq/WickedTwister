package com.example.wickedtwister

import android.os.Parcelable
import android.util.Log
import kotlinx.android.parcel.Parcelize
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.sql.Date
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.CountDownLatch

@Parcelize
class Transaction(
    var accId: String = "",
    var traValue: Float,
    var traDate: LocalDateTime = LocalDateTime.of(1, 1, 1,1,1,1),
    var traName: String = "",
    var traId: String = "",
    var tagName: String = "",
    var accAlias: String = "",
):Parcelable{

    private val logTag = "TransactionAPI"
    private val client = okhttp3.OkHttpClient()
    private val mediaType = "application/json; charset=utf-8".toMediaType()
    private val transactionUrl = "http://10.0.2.2:8000/transaction"

    var status:Boolean = false

    fun create(): Boolean {
        status = false
        val payload = mapOf("acc_id" to accId,
            "tra_date" to traDate,
            "tra_value" to traValue,
            "tag_name" to tagName,
            "tra_name" to traName)
        val requestBody = JSONObject(payload).toString().toRequestBody(mediaType)
        val request: Request = Request.Builder()
            .method("POST", requestBody)
            .url(transactionUrl)
            .build()
        val countDownLatch = CountDownLatch(1)
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.v(logTag, "Failure.")
                status = false
                countDownLatch.countDown()
            }
            override fun onResponse(call: Call, response: Response) {
                when (response.code) {
                    201 ->{
                        Log.d(logTag, "201")
                        val data = jsonToMap(JSONObject(response.body!!.string()))
                        traId = data["tra_id"] as String
                        status = true
                        countDownLatch.countDown()
                    }
                    else -> {
                        Log.v(logTag, response.code.toString())
                        status = false
                        countDownLatch.countDown()
                    }
                }
            }

        })
        countDownLatch.await()
        return status
    }

    fun get(): Boolean {
        status = false
        val request: Request = Request.Builder()
            .url("${transactionUrl}?tra_id=$traId")
            .build()
        val countDownLatch = CountDownLatch(1)
        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                Log.v(logTag, "Failure.")
                status = false
                countDownLatch.countDown()
            }

            override fun onResponse(call: Call, response: Response) {
                when (response.code) {
                    200 -> {
                        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZZZZZ")

                        Log.d(logTag, "200")
                        val data = jsonToMap(JSONObject(response.body!!.string()))
                        traId = data["usr_id"].toString()
                        accId = data["acc_id"].toString()
                        traValue = data["tra_value"].toString().toFloat()
                        traName = data["tra_name"].toString()
                        tagName = data["tag_name"].toString()
                        traDate = LocalDateTime.parse(data["tra_date"], format)
                        status = true
                        countDownLatch.countDown()
                    }
                    else -> {
                        Log.v(logTag, response.code.toString())
                        status = false
                        countDownLatch.countDown()
                    }
                }
            }

        })
        countDownLatch.await()
        return status
    }

    fun update(): Boolean {
        status = false
        val payload = mapOf("tra_id" to traId, "tra_value" to traValue)
        val requestBody = JSONObject(payload).toString().toRequestBody(mediaType)
        val request: Request = Request.Builder()
            .method("PUT", requestBody)
            .url(transactionUrl)
            .build()
        val countDownLatch = CountDownLatch(1)
        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                Log.v(logTag, "Failure.")
                status = false
                countDownLatch.countDown()
            }

            override fun onResponse(call: Call, response: Response) {
                when (response.code) {
                    200 -> {
                        Log.d(logTag, "200")
                        status = true
                        countDownLatch.countDown()
                    }
                    else -> {
                        Log.v(logTag, response.code.toString())
                        status = false
                        countDownLatch.countDown()
                    }
                }

            }

        })
        countDownLatch.await()
        get()
        return status
    }

    fun delete(): Boolean {
        status = false
        val payload = mapOf("tra_id" to traId)
        val requestBody = JSONObject(payload).toString().toRequestBody(mediaType)
        val request: Request = Request.Builder()
            .method("DELETE", requestBody)
            .url(transactionUrl)
            .build()
        val countDownLatch = CountDownLatch(1)
        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                Log.v(logTag, "Failure.")
                status = false
                countDownLatch.countDown()
            }

            override fun onResponse(call: Call, response: Response) {
                when (response.code) {
                    200 -> {
                        Log.d(logTag, "200")
                        status = true
                        countDownLatch.countDown()
                    }
                    else -> {
                        Log.v(logTag, response.code.toString())
                        status = false
                        countDownLatch.countDown()
                    }
                }
            }

        })
        countDownLatch.await()
        return status
    }
}