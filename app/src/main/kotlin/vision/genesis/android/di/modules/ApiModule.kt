package vision.genesis.android.di.modules

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import vision.genesis.android.BuildConfig
import vision.genesis.android.network.services.TradersApi
import vision.genesis.android.network.services.TradersService
import javax.inject.Singleton


@Module
class ApiModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient())
                .baseUrl(BuildConfig.API_ADDRESS)
                .build()
    }

    @Provides
    @Singleton
    fun provideTradersApi(retrofit: Retrofit): TradersApi {
        return retrofit.create<TradersApi>(TradersApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTradersService(api: TradersApi): TradersService {
        return TradersService(api)
    }
}