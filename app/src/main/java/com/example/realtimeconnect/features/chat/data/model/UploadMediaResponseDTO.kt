package com.example.realtimeconnect.features.chat.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadMediaResponseDTO(
    @SerialName("data")
    val uploadMediaData: UploadMediaData?,
    @SerialName("message")
    val message: String?,
    @SerialName("status")
    val status: Boolean?
)


@Serializable
data class UploadMediaData(
    @SerialName("media")
    val media: Media?,
    @SerialName("message")
    val message: Message?
)



@Serializable
data class Media(
    @SerialName("contentType")
    val contentType: String?,
    @SerialName("createdAt")
    val createdAt: String?,
    @SerialName("dmMessageId")
    val dmMessageId: String?,
    @SerialName("fileName")
    val fileName: String?,
    @SerialName("fileSize")
    val fileSize: Int?,
    @SerialName("groupId")
    val groupId: String?,
    @SerialName("groupMessageId")
    val groupMessageId: String?,
    @SerialName("id")
    val id: String?,
    @SerialName("mimeType")
    val mimeType: String?,
    @SerialName("url")
    val url: String?,
    @SerialName("userId")
    val userId: String?
)