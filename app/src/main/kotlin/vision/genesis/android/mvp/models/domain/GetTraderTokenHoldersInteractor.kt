package vision.genesis.android.mvp.models.domain

import io.reactivex.Observable
import vision.genesis.android.mvp.models.data.TokenHolder
import vision.genesis.android.mvp.models.data.repository.TradersRepository


class GetTraderTokenHoldersInteractor(val tradersRepository: TradersRepository) {
    fun execute(traderId: Long): Observable<List<TokenHolder>> {
        return tradersRepository.getTraderTokenHolders(traderId)
    }
}
