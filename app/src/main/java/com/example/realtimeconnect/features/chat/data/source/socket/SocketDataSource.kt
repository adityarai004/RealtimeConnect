package com.example.realtimeconnect.features.chat.data.source.socket

import android.util.Log
import io.socket.client.Ack
import io.socket.client.Socket;
import org.json.JSONObject
import javax.inject.Inject

class SocketDataSource @Inject constructor(private val mSocket: Socket) {
    fun connectSocket(userId: String) {
        try {
            mSocket.off()
            Log.d("TAG", "Connecting...")
            mSocket.on(Socket.EVENT_CONNECT) {
                Log.d("TAG", "Connected to server")
                mSocket.emit("create-connection", userId)
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

    fun sendEvent(event: String, payload: Map<String, Any>) {
        val formattedPayload = JSONObject(payload)
        mSocket.emit(event, formattedPayload)
    }

    fun onMessage(event: String, listener: (JSONObject) -> Unit) {
        mSocket.on(event) { args ->
            Log.d("ARGS", args.toString())
            if (args.isNotEmpty()) {
                val data = args[0] as JSONObject
                listener(data)
            }
        }

    }

    fun joinGroup(groupId: String, userId: String) {
        val payload = JSONObject(mapOf("groupId" to groupId, "userId" to userId))
        mSocket.emit("join-group", payload)
    }

    // Send a group message
    fun sendGroupMessage(payload: Map<String, Any>, ack: (JSONObject) -> Unit) {
        Log.d("Sending Group Message", payload.toString())
        val formattedPayload = JSONObject(payload)
        mSocket.emit("send-group-message", formattedPayload, Ack {
            val ackData = it[0] as JSONObject
            Log.d("Group Message Acknowledgement", ackData.toString())
            ack(ackData)
        })
    }

    // Listen for group messages
    fun onGroupMessage(listener: (JSONObject) -> Unit) {
        mSocket.on("receive-group-messages") { args ->
            Log.d("Group Message Event", args.toString())
            if (args.isNotEmpty()) {
                val message = args[0] as JSONObject
                listener(message)
            }
        }

    }

    // Disconnect from the socket
    fun disconnectSocket() {
        mSocket.disconnect()
    }

}