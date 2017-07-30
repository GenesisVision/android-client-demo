package vision.genesis.android.mvp.models.domain

import io.reactivex.Observable
import vision.genesis.android.mvp.models.data.TraderInfo
import vision.genesis.android.mvp.models.data.repository.TradersRepository

class GetTradersListInteractor(tradersRepository: TradersRepository) {
    val tradersRepository: TradersRepository = tradersRepository

    fun execute(skip: Int): Observable<List<TraderInfo>> {
        return tradersRepository.getTraderList(skip)
    }
}
