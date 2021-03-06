package vision.genesis.android.mvp.models.data.repository

import io.reactivex.Observable
import vision.genesis.android.GenesisVisionApp
import vision.genesis.android.mvp.models.data.PaymentInfo
import vision.genesis.android.mvp.models.data.TokenHolder
import vision.genesis.android.mvp.models.data.TraderGraphics
import vision.genesis.android.mvp.models.data.TraderInfo
import vision.genesis.android.network.services.TradersService
import javax.inject.Inject

class TradersNetworkRepository(val service: TradersService): TradersRepository {

    override fun getTraderList(skip: Int): Observable<List<TraderInfo>> {
        return service.getTradersList(skip).flatMap {
            if (it.isSuccess) {
                Observable.just(it.result)
            } else {
                Observable.error(Exception(it.status))
            }
        }
    }

    override fun getTraderGraphics(traderId: Long): Observable<List<TraderGraphics>> {
        return service.getTraderGraphics(traderId).flatMap {
            if (it.isSuccess) {
                Observable.just(it.result)
            } else {
                Observable.error(Exception(it.status))
            }
        }
    }

    override fun getTraderTokenHolders(traderId: Long): Observable<List<TokenHolder>> {
        return service.getTraderTokenHolders(traderId).flatMap {
            if (it.isSuccess) {
                Observable.just(it.result)
            } else {
                Observable.error(Exception(it.status))
            }
        }
    }

    override fun getPaymentInfo(amount: Float): Observable<PaymentInfo> {
        return service.getPaymentInfo(amount).flatMap {
            if (it.isSuccess) {
                Observable.just(it.result)
            } else {
                Observable.error(Exception(it.status))
            }
        }
    }

    override fun getGvtValue(address: String): Observable<Float> {
        return service.getGvtValue(address).flatMap {
            if (it.isSuccess) {
                Observable.just(it.gvtValue)
            } else {
                Observable.error(Exception(it.status))
            }
        }
    }
}
