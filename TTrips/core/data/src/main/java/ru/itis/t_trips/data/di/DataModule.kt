package ru.itis.t_trips.data.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.itis.t_trips.data.local.LocalDatabase
import ru.itis.t_trips.data.local.dao.ExpenseReceiptUrlDao
import ru.itis.t_trips.data.local.dao.TripPictureUrlDao
import ru.itis.t_trips.utils.PrefProperties
import javax.inject.Singleton
import ru.itis.t_trips.data.BuildConfig as dataConfig

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PrefProperties.SHARED_PREF_NAME)

    @Provides
    @Singleton
    internal fun provideDatastorePreferences(application: Application): DataStore<Preferences> {
        return application.dataStore
    }

    @Provides
    @Singleton
    internal fun provideDatabase(@ApplicationContext context: Context): LocalDatabase {
        return Room.databaseBuilder(
            context,
            LocalDatabase::class.java,
            dataConfig.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    internal fun provideTripPictureUrlDao(database: LocalDatabase): TripPictureUrlDao {
        return database.tripPictureUrlDao
    }

    @Provides
    @Singleton
    internal fun provideExpenseReceiptUrlDao(database: LocalDatabase): ExpenseReceiptUrlDao {
        return database.expenseReceiptUrlDao
    }
}