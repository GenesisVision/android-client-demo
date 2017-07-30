package vision.genesis.android.di

import dagger.Component
import vision.genesis.android.di.modules.ApiModule
import vision.genesis.android.di.modules.ContextModule
import vision.genesis.android.di.modules.InteractorsModule
import vision.genesis.android.di.modules.NavigationModule
import vision.genesis.android.mvp.models.data.repository.TradersRepository
import vision.genesis.android.mvp.presenters.TradersListPresenter
import vision.genesis.android.ui.activities.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        NavigationModule::class,
        ContextModule::class,
        InteractorsModule::class
))
interface AppComponent {
    fun inject(activity: MainActivity)

    fun inject(activity: TradersListPresenter)

    fun inject(activity: TradersRepository)
}