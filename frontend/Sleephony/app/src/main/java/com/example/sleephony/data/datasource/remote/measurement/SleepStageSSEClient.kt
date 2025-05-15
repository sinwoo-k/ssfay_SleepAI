package com.example.sleephony.data.datasource.remote.measurement

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
    private val lineBuffer = Buffer()

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun connect(requestDto: SleepBioDataRequest) {
        // Cancel previous connection if any
        call?.cancel()

        // Serialize request body
        val json = Gson().toJson(requestDto)
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body: RequestBody = json.toRequestBody(mediaType)

        // Build URL safely
        val url = baseUrl.toHttpUrl()
            .newBuilder()
            .addPathSegments("sleep/stage/raw")
            .build()

        val request = Request.Builder()
            .url(url)
            .post(body)
            .header("Accept", "text/event-stream")
            .header("Authorization", "Bearer $token")
            .build()

        call = client.newCall(request).also { call ->
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    listener?.onError("SSE connection failed: ${e.message}")
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use { resp ->
                        if (!resp.isSuccessful) {
                            listener?.onError("Unexpected response: HTTP ${resp.code}")
                            return
                        }

                        val source: BufferedSource = resp.body?.source() ?: run {
                            listener?.onError("Empty response body")
                            return
                        }

                        parseSse(source, call)
                    }
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
                source.read(lineBuffer, 8192)
                var line: String?
                while (lineBuffer.readUtf8Line().also { line = it } != null) {
                    when {
                        line!!.startsWith("id:") -> id = line!!.substring(3).trim()
                        line!!.startsWith("event:") -> event = line!!.substring(6).trim()
                        line!!.startsWith("data:") -> data = line!!.substring(5).trim()
                        line!!.isEmpty() && event != null && data != null -> {
                            if (event == "sleepStage") {
                                listener?.onSleepStageReceived(id, data)
                            }
                            id = null
                            event = null
                            data = null
                        }
                    }
                }
                lineBuffer.clear()
            }
            listener?.onComplete()
        } catch (e: Exception) {
            if (!call.isCanceled()) {
                listener?.onError("Error processing SSE: ${e.message}")
            }
        }
    }

    fun disconnect() {
        call?.cancel()
        call = null
    }
}
