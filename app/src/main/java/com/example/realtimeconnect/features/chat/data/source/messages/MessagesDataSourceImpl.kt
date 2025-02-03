package com.example.realtimeconnect.features.chat.data.source.messages

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.example.realtimeconnect.core.constants.NetworkConstants
import com.example.realtimeconnect.core.constants.NetworkConstants.DMS
import com.example.realtimeconnect.core.datastore.DataStoreHelper
import com.example.realtimeconnect.features.chat.data.model.DMResponseDTO
import com.example.realtimeconnect.features.chat.data.model.GroupChatListResponseDTO
import com.example.realtimeconnect.features.chat.data.model.UploadMediaResponseDTO
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.cio.Response
import java.io.File
import javax.inject.Inject

class MessagesDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val dataStoreHelper: DataStoreHelper
) : MessagesDataSource {
    override suspend fun getMessages(receiverId: String, timestamp: String): DMResponseDTO {
        val response = httpClient.get(DMS) {
            parameter("receiverId", receiverId)
            parameter("timestamp", timestamp)
        }.body<DMResponseDTO>()

        return response
    }

    override suspend fun getGroupMessages(
        page: Int,
        perPage: Int,
        groupId: String
    ): GroupChatListResponseDTO {
        val response = httpClient.get(NetworkConstants.GROUP_MESSAGES) {
            url {
                parameter(key = "page", value = page)
                parameter(key = "perPage", value = perPage)
                parameter(key = "groupId", value = groupId)
            }
        }.body<GroupChatListResponseDTO>()
        return response
    }

    override suspend fun uploadMedia(
        receiverId: String,
        content: String,
        filePath: String,
        fileName: String,
        mimeType: String
    ): UploadMediaResponseDTO {
        val response = httpClient.post(NetworkConstants.UPLOAD_IMAGES) {
            header(HttpHeaders.Authorization, "Bearer ${dataStoreHelper.getUserAuthKey()}")

            setBody(
                MultiPartFormDataContent(
                    formData {
                        append(
                            "image",
                            File(filePath).readBytes(),
                            Headers.build {
                                append(
                                    HttpHeaders.ContentDisposition,
                                    "name=\"image\"; filename=${fileName}"
                                )
                                append(HttpHeaders.ContentType, mimeType)
                            }
                        )
                        append("receiverId", receiverId)
                        append("content", content)
                    },
                    boundary = "WebAppBoundary"
                )
            )
            onUpload { bytesSent, contentLength ->
                println("Upload progress: $bytesSent bytes sent out of $contentLength")
            }
        }.body<UploadMediaResponseDTO>()

        return response
    }
}