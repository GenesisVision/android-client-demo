package vision.genesis.android.mvp.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.*
import vision.genesis.android.mvp.models.data.TraderInfo

@StateStrategyType(AddToEndSingleStrategy::class)
interface TradersListView : MvpView {
    fun showError(message: String)
    fun hideError()

    fun showToolbar()

    fun onStartLoading()
    fun onFinishLoading()

    fun showRefreshing()
    fun hideRefreshing()

    fun showListProgress()
    fun hideListProgress()
    fun checkAddressDialog()

    fun setTraders(traders: List<TraderInfo>)

    fun showGvtValue(gvt: Float)

    @StateStrategyType(AddToEndStrategy::class)
    fun addTraders(traders: List<TraderInfo>)
}