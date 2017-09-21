package vision.genesis.android.mvp.models.domain

import io.reactivex.Observable
import vision.genesis.android.mvp.models.data.PaymentInfo
import vision.genesis.android.mvp.models.data.repository.TradersRepository


class GetPaymentInfoInteractor(val tradersRepository: TradersRepository) {
    fun execute(amount: Int): Observable<PaymentInfo> {
        return tradersRepository.getPaymentInfo(amount)
    }
}

