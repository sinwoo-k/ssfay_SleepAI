package com.example.sleephony.data.datasource.remote.measurement

import android.util.Log
import com.example.sleephony.data.model.measurement.SleepBioDataRequest
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.Buffer
import okio.BufferedSource
import java.io.IOException
import java.util.concurrent.TimeUnit

class SleepStageSSEClient(
    private val baseUrl: String,
    private val token: String
) {

    interface Listener {
        fun onOpen()
        fun onSleepStageReceived(requestId: String?, sleepStage: String)
        fun onError(message: String)
        fun onComplete()
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(90, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    private var call: Call? = null
    private var listener: Listener? = null

    fun setListener(listener: Listener) {
        this.listener = listener
    }


    fun connect(requestDto: SleepBioDataRequest) {
        call?.cancel()

        val json = Gson().toJson(requestDto)
        val body = json.toRequestBody("application/json; charset=utf-8".toMediaType())

        val url = baseUrl.toHttpUrl()
            .newBuilder()
            .addPathSegments("sleep/stage/raw")
            .build()

        val req = Request.Builder()
            .url(url)
            .post(body)
            .header("Accept", "text/event-stream")
            .header("Authorization", "Bearer $token")
            .build()

        call = client.newCall(req).also { call ->
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    listener?.onError("SSE connection failed: ${e.message}")
                }
                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        val errorBody = response.body?.string()
                        listener?.onError("Unexpected HTTP ${response.code} : $errorBody")
                        return
                    }
                    listener?.onOpen()
                    val source = response.body?.source() ?: run {
                        listener?.onError("Empty SSE body")
                        return
                    }
                    parseSse(source, call)
                }
            })
        }
    }

    private fun parseSse(source: BufferedSource, call: Call) {
        var id: String? = null
        var event: String? = null
        var data: String? = null

        try {
            while (!source.exhausted() && !call.isCanceled()) {
                val line = source.readUtf8LineStrict()
                when {
                    line.startsWith("id:")    -> id    = line.drop(3).trim()
                    line.startsWith("event:") -> event = line.drop(6).trim()
                    line.startsWith("data:")  -> data  = line.drop(5).trim()
                    line.isEmpty() && event != null && data != null -> {
                        if (event == "sleepStage") {
                            var cleanStage = data.removeSurrounding("\"")
                            listener?.onSleepStageReceived(id, cleanStage)
                        }
                        id = null; event = null; data = null
                    }
                }
            }
            listener?.onComplete()
        } catch (e: Exception) {
            if (!call.isCanceled()) listener?.onError("SSE parse error: ${e.message}")
        }
    }

    fun disconnect() {
        call?.cancel()
        call = null
    }
}
