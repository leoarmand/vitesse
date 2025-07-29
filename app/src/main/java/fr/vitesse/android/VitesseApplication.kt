package fr.vitesse.android

import android.app.Application
import fr.vitesse.android.module.ActionComposerModule
import fr.vitesse.android.module.DatabaseModule
import fr.vitesse.android.module.candidateDetailsViewModelModule
import fr.vitesse.android.module.candidateRepositoryModule
import fr.vitesse.android.module.candidateServiceModule
import fr.vitesse.android.module.homeViewModelModule
import fr.vitesse.android.module.httpClientModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class VitesseApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)
            androidLogger()
            modules(
                DatabaseModule(applicationContext).module,
                ActionComposerModule(applicationContext).module,
                httpClientModule,
                candidateRepositoryModule,
                candidateServiceModule,
                candidateDetailsViewModelModule,
                homeViewModelModule
            )
        }
    }
}
