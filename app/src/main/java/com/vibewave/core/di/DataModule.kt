package com.vibewave.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.vibewave.data.db.HistoryDao
import com.vibewave.data.db.VibeWaveDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// DataStore is created via a property delegate on Context; this is the
// Android-recommended pattern and gives us a single long-lived instance.
private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "vibewave_settings")

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): VibeWaveDatabase =
        Room.databaseBuilder(ctx, VibeWaveDatabase::class.java, "vibewave.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideHistoryDao(db: VibeWaveDatabase): HistoryDao = db.historyDao()

    @Provides
    fun provideFavoriteDao(db: VibeWaveDatabase): com.vibewave.data.db.FavoriteDao =
        db.favoriteDao()

    @Provides
    @Singleton
    fun provideSettingsDataStore(@ApplicationContext ctx: Context): DataStore<Preferences> =
        ctx.settingsDataStore
}
