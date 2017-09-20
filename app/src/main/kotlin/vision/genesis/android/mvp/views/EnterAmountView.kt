package vision.genesis.android.mvp.views

import com.arellomobile.mvp.MvpView
import vision.genesis.android.mvp.models.data.TraderInfo


interface EnterAmountView : MvpView {
    fun showTraderInfo(traderInfo: TraderInfo)
    fun showUserInfo(availableTokens: Int, bidForToken: Float)
}