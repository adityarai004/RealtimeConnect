package com.example.realtimeconnect.features.chat.data.local.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.realtimeconnect.features.chat.data.local.dao.MessagesDao
import com.example.realtimeconnect.features.chat.data.local.entity.MessageEntity

@Database(
    entities = [MessageEntity::class],
    version = 2,
    autoMigrations = [AutoMigration(from = 1, to = 2)],
    exportSchema = true
)
abstract class RTCDatabase : RoomDatabase() {
    abstract fun messageDao(): MessagesDao
}