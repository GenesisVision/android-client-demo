package vision.genesis.android.di

import dagger.Component
import vision.genesis.android.di.modules.ApiModule
import vision.genesis.android.di.modules.ContextModule
import vision.genesis.android.di.modules.InteractorsModule
import vision.genesis.android.di.modules.NavigationModule
import vision.genesis.android.mvp.models.data.repository.TradersRepository
import vision.genesis.android.mvp.presenters.EnterAmountPresenter
import vision.genesis.android.mvp.presenters.PaymentConfirmationPresenter
import vision.genesis.android.mvp.presenters.TraderProfilePresenter
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

    fun inject(presenter: TradersListPresenter)
    fun inject(presenter: TraderProfilePresenter)
    fun inject(presenter: EnterAmountPresenter)
    fun inject(presenter: PaymentConfirmationPresenter)

    fun inject(repository: TradersRepository)
}