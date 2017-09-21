package vision.genesis.android.network.services

import io.reactivex.Observable
import vision.genesis.android.extensions.observeOnMainThread
import vision.genesis.android.extensions.subscribeOnIoThread
import vision.genesis.android.mvp.models.data.PaymentInfo
import vision.genesis.android.mvp.models.data.TokenHolder
import vision.genesis.android.mvp.models.data.TraderGraphics
import vision.genesis.android.mvp.models.data.TraderInfo
import vision.genesis.android.network.ApiResponse

class TradersService(api: TradersApi) {
    val api: TradersApi = api
    fun getTradersList(skip: Int): Observable<ApiResponse<List<TraderInfo>>> {
        return api.getTradersList(skip).subscribeOnIoThread().observeOnMainThread()
    }

    fun getTraderGraphics(traderId: Long): Observable<ApiResponse<List<TraderGraphics>>> {
        return api.getTraderGraphics(traderId).subscribeOnIoThread().observeOnMainThread()
    }

    fun getTraderTokenHolders(traderId: Long): Observable<ApiResponse<List<TokenHolder>>> {
        return api.getTraderTokenHolders(traderId).subscribeOnIoThread().observeOnMainThread()
    }

    fun getPaymentInfo(amount: Int): Observable<ApiResponse<PaymentInfo>> {
        return api.getPaymentInfo(amount).subscribeOnIoThread().observeOnMainThread()
    }
}
