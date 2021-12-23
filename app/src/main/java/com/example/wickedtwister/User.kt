package com.example.wickedtwister

import android.os.Parcelable
import android.util.Log
import com.github.mikephil.charting.data.PieEntry
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
class User(var usrId: String = "",
           var usrDoc:String = "",
           var usrPwd:String = "",
           var usrEmail: String = "",
           var usrNickname:String = ""):Parcelable{

    private val logTag = "UserAPI"
    private val client = okhttp3.OkHttpClient()
    private val mediaType = "application/json; charset=utf-8".toMediaType()
    private val usrUrl = "http://10.0.2.2:8000/user"
    private val usrSerialUrl = "http://10.0.2.2:8000/user/serial"
    private val accountUrl = "http://10.0.2.2:8000/account"

    var accounts:MutableMap<String, Account> = mutableMapOf()

    var status:Boolean = false

    var serialTra:MutableMap<String, Float> = mutableMapOf()

    var serialBud:MutableMap<String, Float> = mutableMapOf()

    var transactions:MutableList<Transaction> = mutableListOf()

    private fun loginUrl(): String {
        return "$usrUrl?usr_doc=$usrDoc&usr_pwd=$usrPwd"
    }

    private fun accountUrlUsr(): String {
        return "$accountUrl?usr_id=$usrId"
    }

    fun create() {
        status = false
        val payload = mapOf("usr_nickname" to usrNickname, "usr_email" to usrEmail,
            "usr_doc" to usrDoc,
            "usr_pwd" to usrPwd)
        val requestBody = JSONObject(payload).toString().toRequestBody(mediaType)
        val request: Request = Request.Builder()
            .method("POST", requestBody)
            .url(usrUrl)
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

    fun login() {
        status = false
        val logUrl = loginUrl()
        val request: Request = Request.Builder()
            .url(logUrl)
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
                        usrId = jsonToMap(JSONObject(response.body!!.string()))["usr_id"] as String
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

    fun get() {
        status = false
        val request: Request = Request.Builder()
            .url("$usrUrl?usr_id=$usrId")
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
                        usrDoc = data["usr_doc"] as String
                        usrPwd = data["usr_pwd"] as String
                        usrEmail = data["usr_email"] as String
                        usrNickname = data["usr_nickname"] as String
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

    fun update() {
        status = false
        val payload = mapOf("usr_id" to usrId, "usr_email" to usrEmail)
        val requestBody = JSONObject(payload).toString().toRequestBody(mediaType)
        val request: Request = Request.Builder()
            .method("PUT", requestBody)
            .url(usrUrl)
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
    }

    fun delete() {
        status = false
        val payload = mapOf("usr_id" to usrId)
        val requestBody = JSONObject(payload).toString().toRequestBody(mediaType)
        val request: Request = Request.Builder()
            .method("DELETE", requestBody)
            .url(usrUrl)
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
    }

    fun getAccounts() {
        val request: Request = Request.Builder()
            .url(accountUrlUsr())
            .build()
        val countDownLatch = CountDownLatch(1)
        accounts = mutableMapOf()
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
                        val accMap = jsonToMap(JSONObject(response.body!!.string()))

                        for (key in accMap.keys){
                            val item = JSONObject(accMap[key].toString())
                            val acc = Account(
                                usrId = usrId,
                                accAlias = item["acc_alias"] as String,
                                accId = item["acc_id"] as String,
                                tagName = item["tag_name"] as String
                            )
                            accounts[item["acc_id"] as String] = acc
                        }

                    }
                    else -> {
                        Log.v(logTag, response.code.toString())
                        status = false
                        countDownLatch.countDown()
                    }
                }
            }
        })
    }

    fun serial(){
        status = false
        val request: Request = Request.Builder()
            .url("$usrSerialUrl?usr_id=$usrId")
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

                        for (item in entriesDataTra){
                            serialTra[item.key] = item.value.toFloat()
                        }

                        for (item in entriesDataBud){
                            serialBud[item.key] = item.value.toFloat()
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