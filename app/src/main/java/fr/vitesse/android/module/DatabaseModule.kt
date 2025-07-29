package fr.vitesse.android.module

import android.content.Context
import androidx.room.Room
import fr.vitesse.android.data.AppDatabase
import org.koin.dsl.module

class DatabaseModule (context: Context) {
    val appDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "vitesse-db"
    ).build()

    val module = module {
        single{ appDatabase }
        single{ appDatabase.candidateDao() }
    }
}
