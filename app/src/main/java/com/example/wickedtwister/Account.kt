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
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.CountDownLatch

@Parcelize
class Account(var usrId:String = "",
              var accId:String = "",
              var tagName:String = "",
              var accAlias:String = ""):Parcelable{

    private val logTag = "AccountAPI"
    private val client = okhttp3.OkHttpClient()
    private val mediaType = "application/json; charset=utf-8".toMediaType()
    private val accountUrl = "http://10.0.2.2:8000/account"
    private val accountSerialUrl = "http://10.0.2.2:8000/account/serial"
    private val transactionUrl = "http://10.0.2.2:8000/transaction"



    var status:Boolean = false

    var serialTra:MutableMap<String, Float> = mutableMapOf()

    var serialBud:MutableMap<String, Float> = mutableMapOf()

    var transactions:MutableList<Transaction> = mutableListOf()

    private fun transactionUrlAcc(): String {
        return "$transactionUrl?acc_id=$accId"
    }
    fun create(): Boolean {
        status = false
        val payload = mapOf("usr_id" to usrId,
            "acc_alias" to accAlias,
            "tag_name" to tagName)
        val requestBody = JSONObject(payload).toString().toRequestBody(mediaType)
        val request: Request = Request.Builder()
            .method("POST", requestBody)
            .url(accountUrl)
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
                        accId = data["acc_id"] as String
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
            .url("${accountUrl}?acc_id=$accId")
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
                        val data = jsonToMap(JSONObject(response.body!!.string()))
                        usrId = data["usr_id"] as String
                        accAlias = data["acc_alias"] as String
                        accId = data["acc_id"] as String
                        tagName = data["tag_name"] as String
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
        val payload = mapOf("usr_id" to usrId, "acc_alias" to accAlias)
        val requestBody = JSONObject(payload).toString().toRequestBody(mediaType)
        val request: Request = Request.Builder()
            .method("PUT", requestBody)
            .url(accountUrl)
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

    fun delete(): Boolean {
        status = false
        val payload = mapOf("acc_id" to accId)
        val requestBody = JSONObject(payload).toString().toRequestBody(mediaType)
        val request: Request = Request.Builder()
            .method("DELETE", requestBody)
            .url(accountUrl)
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

    fun getTransactions() {
        val request: Request = Request.Builder()
            .url(transactionUrlAcc())
            .build()
        val countDownLatch = CountDownLatch(1)
        transactions = mutableListOf()
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

                        val traMap = jsonToMap(JSONObject(response.body!!.string()))

                        for (key in traMap.keys){
                            val item = JSONObject(traMap[key] as String)
                            transactions.add(Transaction(accId = accId,
                                traId = item["tra_id"] as String,
                                traDate = item["tra_date"] as String,
                                traName = item["tra_name"] as String,
                                traValue = item["tra_value"] as String,
                                tagName = item["tag_name"] as String,
                                accAlias = accAlias))
                        }

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
    }

    fun serial(){
        status = false
        val request: Request = Request.Builder()
            .url("$accountSerialUrl?acc_id=$accId")
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
                        val answer = jsonToMap(JSONObject(response.body!!.string()))

                        val entriesDataTra = jsonToMap(JSONObject(answer["pieEntriesTra"].toString()))
                        val entriesDataBud = jsonToMap(JSONObject(answer["pieEntriesBud"].toString()))
                        val transactionsData = jsonToMap(JSONObject(answer["transactions"].toString()))

                        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZZZZZ")

                        for (item in entriesDataBud){
                            serialBud[item.key] = item.value.toFloat()
                            serialTra[item.key] = 0F
                        }

                        for (item in entriesDataTra){
                            serialTra[item.key] = item.value.toFloat()
                        }

                        for (item in transactionsData){
                            val newItem = jsonToMap(JSONObject(item.value))
                            transactions.add(Transaction(accId = newItem["acc_id"].toString(),
                                traId = newItem["tra_id"].toString(),
                                traDate = LocalDate.parse(newItem["tra_date"], format).toString(),
                                traName = newItem["tra_name"].toString(),
                                traValue = newItem["tra_value"].toString(),
                                tagName = newItem["tag_name"].toString())
                            )
                        }

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
    }
}