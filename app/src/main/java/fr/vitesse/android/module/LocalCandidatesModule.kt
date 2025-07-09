package fr.vitesse.android.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.vitesse.android.data.AppDatabase
import fr.vitesse.android.data.CandidateDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalCandidatesModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "vitesse-db"
        ).build()
    }

    @Provides
    fun provideCandidateDao(db: AppDatabase): CandidateDao = db.candidateDao()
}
