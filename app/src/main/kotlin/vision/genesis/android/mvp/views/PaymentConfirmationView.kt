package vision.genesis.android.mvp.views

import com.arellomobile.mvp.MvpView
import vision.genesis.android.mvp.models.data.AccountInfo
import vision.genesis.android.mvp.models.data.PaymentInfo
import vision.genesis.android.mvp.models.data.TraderInfo


interface PaymentConfirmationView: MvpView {
    fun showTraderInfo(trader: TraderInfo)
    fun showPaymentInfo(info: PaymentInfo)
    fun showAccountInfo(info: AccountInfo)
    fun showError(error: String)
}