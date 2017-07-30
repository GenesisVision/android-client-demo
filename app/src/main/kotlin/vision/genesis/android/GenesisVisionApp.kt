package vision.genesis.android

import android.app.Application
import vision.genesis.android.di.AppComponent
import vision.genesis.android.di.DaggerAppComponent
import vision.genesis.android.di.modules.ApiModule
import vision.genesis.android.di.modules.InteractorsModule
import vision.genesis.android.di.modules.NavigationModule

class GenesisVisionApp : Application() {
    companion object {
        @JvmStatic private lateinit var sApplicationComponent: AppComponent
        fun getAppComponent(): AppComponent {
            return sApplicationComponent
        }
    }

    override fun onCreate() {
        createApplicationComponent()
        super.onCreate()
    }

    private fun createApplicationComponent() {
        sApplicationComponent = DaggerAppComponent.builder()
                .interactorsModule(InteractorsModule())
                .navigationModule(NavigationModule())
                .build()
    }

}
