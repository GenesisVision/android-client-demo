package vision.genesis.android.mvp.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import vision.genesis.android.mvp.models.data.TraderGraphics
import vision.genesis.android.mvp.models.data.TraderInfo


@StateStrategyType(AddToEndSingleStrategy::class)
interface TraderProfileView : MvpView {
    fun showTraderInfo(traderInfo: TraderInfo)
    fun showTraderGraphics(graphics: List<TraderGraphics>)
    fun showSelectedTraderGraphic(graphic: TraderGraphics, selectedItem: Int)
    fun showError(message: String)
    fun hideLoading()
}