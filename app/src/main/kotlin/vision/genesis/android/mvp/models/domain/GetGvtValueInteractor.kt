package vision.genesis.android.mvp.models.domain

import io.reactivex.Observable
import vision.genesis.android.mvp.models.data.repository.TradersRepository

class GetGvtValueInteractor(val tradersRepository: TradersRepository) {
    fun execute(address: String): Observable<Float> {
        return tradersRepository.getGvtValue(address)
    }
}
