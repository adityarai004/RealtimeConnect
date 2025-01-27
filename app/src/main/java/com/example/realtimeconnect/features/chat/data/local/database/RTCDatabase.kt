package com.example.realtimeconnect.features.chat.data.local.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.realtimeconnect.features.chat.data.local.dao.MessagesDao
import com.example.realtimeconnect.features.chat.data.local.entity.MediaEntity
import com.example.realtimeconnect.features.chat.data.local.entity.MessageEntity

@Database(
    entities = [MessageEntity::class, MediaEntity::class],
    version = 3,
    autoMigrations = [AutoMigration(from = 2, to = 3)],
    exportSchema = true
)
abstract class RTCDatabase : RoomDatabase() {
    abstract fun messageDao(): MessagesDao
}