package com.example.realtimeconnect.chat.data.source

import android.util.Log
import com.example.realtimeconnect.core.constants.NetworkConstants.BASE_URL
import io.socket.client.Ack
import io.socket.client.IO;
import io.socket.client.Socket;
import org.json.JSONObject


class SocketDataSource {
    private lateinit var mSocket: Socket
    fun connectSocket(userId: String, senderId: String) {
        try {
            Log.d("TAG", "Connecting...")
            mSocket = IO.socket(BASE_URL)

            mSocket.on(Socket.EVENT_CONNECT) {
                Log.d("TAG", "Connected to server")
                mSocket.emit("create-connection", userId)
                mSocket.emit("message-seen", JSONObject(mapOf("senderId" to senderId, "viewerId" to userId )))
            }

            mSocket.on(Socket.EVENT_CONNECT_ERROR) { args ->
                Log.e("TAG", "Connection Error: ${args.joinToString()}")
            }

            mSocket.on(Socket.EVENT_DISCONNECT) {
                Log.d("TAG", "Disconnected from server")
            }

            mSocket.connect()
        } catch (e: Exception) {
            Log.e("SocketError", "Socket Error: ${e.message}")
        }
    }

    fun sendMessage(payload: Map<String, Any>, ack: (JSONObject) -> Unit) {
        Log.d("Sending Map", payload.toString())
        val formattedPayload = JSONObject(payload)
        mSocket.emit("send-message", formattedPayload, Ack {
            val ackData = it[0] as JSONObject
            Log.e("Acknowledgement", "ackData ${ackData["sent"]} timestamp ${ackData["timestamp"]}")
            ack(ackData)
        })
    }

    fun sendEvent(event: String, payload: Map<String, Any>){
        val formattedPayload = JSONObject(payload)
        mSocket.emit(event, formattedPayload)
    }

    fun onMessage(event: String, listener: (JSONObject) -> Unit) {
        if (::mSocket.isInitialized) {
            mSocket.on(event) { args ->
                Log.d("ARGS", args.toString())
                if (args.isNotEmpty()) {
                    val data = args[0] as JSONObject
                    listener(data)
                }
            }
        }
    }
}