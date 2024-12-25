package com.example.realtimeconnect.core.di


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.realtimeconnect.MainViewModel
import com.example.realtimeconnect.core.datastore.DataStoreHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatastore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("discordclone")
        }
    }

    @Provides
    @Singleton
    fun provideMainViewModel(dataStoreHelper: DataStoreHelper): MainViewModel {
        return MainViewModel(dataStoreHelper)
    }
}