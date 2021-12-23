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
import java.util.concurrent.CountDownLatch

@Parcelize
class Tag(var tagName: String = "",
           var tagType:String = "",
           var imgName:String = "",
           var tagColour: String = ""): Parcelable {

    private val logTag = "TagAPI"
    private val client = okhttp3.OkHttpClient()
    private val mediaType = "application/json; charset=utf-8".toMediaType()
    private val tagUrl = "http://10.0.2.2:8000/tag"


    var status:Boolean = false


    fun create() {
        status = false
        val payload = mapOf("tag_name" to tagName, "tag_type" to tagType,
            "img_name" to imgName,
            "tag_colour" to tagColour)
        val requestBody = JSONObject(payload).toString().toRequestBody(mediaType)
        val request: Request = Request.Builder()
            .method("POST", requestBody)
            .url(tagUrl)
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

    fun get() {
        status = false
        val request: Request = Request.Builder()
            .url("$tagUrl?tag_name=$tagName")
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
                        tagName = data["tag_name"] as String
                        tagType = data["tag_type"] as String
                        imgName = data["img_name"] as String
                        tagColour = data["tag_colour"] as String
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
        val payload = mapOf("tag_name" to tagName, "tag_type" to tagType,
            "img_name" to imgName,
            "tag_colour" to tagColour)
        val requestBody = JSONObject(payload).toString().toRequestBody(mediaType)
        val request: Request = Request.Builder()
            .method("PUT", requestBody)
            .url("$tagUrl?tag_name=$tagName")
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
        val payload = mapOf("tag_name" to tagName)
        val requestBody = JSONObject(payload).toString().toRequestBody(mediaType)
        val request: Request = Request.Builder()
            .method("DELETE", requestBody)
            .url("$tagUrl?tag_name=$tagName")
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
}