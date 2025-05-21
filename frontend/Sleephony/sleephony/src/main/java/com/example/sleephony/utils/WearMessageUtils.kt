package com.example.sleephony.utils

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.json.JSONObject

object WearMessageUtils {
    fun SendMessage(
        context: Context,
        mode:String,
        data:Map<String,Any?>
    ){
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val nodeClient = Wearable.getNodeClient(context)
                val messageClient = Wearable.getMessageClient(context)

                val jsonData = JSONObject().apply {
                    put("mode",mode)
                    data?.forEach{(key,value) ->
                        put(key,value)
                    }
                }
                val jsonString = jsonData.toString()

                val nodes = nodeClient.connectedNodes.await()
                for (node in nodes) {
                    messageClient.sendMessage(
                        node.id,
                        "/alarm",
                        "$jsonString ".toByteArray()
                    ).await()
                }
            } catch (error: Exception){
                Log.e("ssafy","$error")
            }
        }
    }
}