package vision.genesis.android.mvp.models.domain

import io.reactivex.Observable
import vision.genesis.android.mvp.models.data.TraderGraphics
import vision.genesis.android.mvp.models.data.repository.TradersRepository

class GetTraderGraphicsInteractor(val tradersRepository: TradersRepository) {
    fun execute(traderId: Long): Observable<List<TraderGraphics>> {
        return tradersRepository.getTraderGraphics(traderId)
    }
}

