package vision.genesis.android.mvp.models.data.repository

import io.reactivex.Observable
import vision.genesis.android.GenesisVisionApp
import vision.genesis.android.mvp.models.data.TraderInfo
import vision.genesis.android.network.services.TradersService
import javax.inject.Inject

class TradersNetworkRepository(service: TradersService): TradersRepository {

    val service: TradersService = service

    override fun getTraderList(skip: Int): Observable<List<TraderInfo>> {
        return service.getTradersList(skip).flatMap {
            if (it.isSuccess) {
                Observable.just(it.result)
            } else {
                Observable.error(Exception(it.status))
            }
        }
    }
}
