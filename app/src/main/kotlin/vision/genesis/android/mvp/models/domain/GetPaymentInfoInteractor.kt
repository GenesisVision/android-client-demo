package vision.genesis.android.mvp.models.domain

import io.reactivex.Observable
import vision.genesis.android.mvp.models.data.PaymentInfo
import vision.genesis.android.mvp.models.data.repository.TradersRepository


class GetPaymentInfoInteractor(val tradersRepository: TradersRepository) {
    fun execute(amount: Float): Observable<PaymentInfo> {
        return tradersRepository.getPaymentInfo(amount)
    }
}

