package fr.vitesse.android

import android.app.Application
import fr.vitesse.android.module.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class VitesseApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)
            androidLogger()
            modules(AppModule(applicationContext).modules)
        }
    }
}
