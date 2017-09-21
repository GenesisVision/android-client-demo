package vision.genesis.android.di.modules

import dagger.Module
import dagger.Provides
import vision.genesis.android.mvp.models.data.repository.TradersNetworkRepository
import vision.genesis.android.mvp.models.data.repository.TradersRepository
import vision.genesis.android.mvp.models.domain.*
import vision.genesis.android.network.services.TradersService
import javax.inject.Singleton

@Module(includes = arrayOf(ApiModule::class))
class InteractorsModule {
    @Provides
    @Singleton
    fun provideTradersRepository(tradersService: TradersService): TradersRepository {
       return TradersNetworkRepository(tradersService)
    }

    @Provides
    @Singleton
    fun provideGetTradersInteractor(tradersRepository: TradersRepository):GetTradersListInteractor {
        return GetTradersListInteractor(tradersRepository)
    }

    @Provides
    @Singleton
    fun provideGetTraderGraphicsInteractor(tradersRepository: TradersRepository):GetTraderGraphicsInteractor {
        return GetTraderGraphicsInteractor(tradersRepository)
    }

    @Provides
    @Singleton
    fun provideGetTraderTokenHolderInteractor(tradersRepository: TradersRepository):GetTraderTokenHoldersInteractor {
        return GetTraderTokenHoldersInteractor(tradersRepository)
    }

    @Provides
    @Singleton
    fun provideGetPaymentInfoInteractor(tradersRepository: TradersRepository):GetPaymentInfoInteractor {
        return GetPaymentInfoInteractor(tradersRepository)
    }

    @Provides
    @Singleton
    fun provideAccountInteractor():AccountInteractor {
        return AccountInteractor()
    }
}
