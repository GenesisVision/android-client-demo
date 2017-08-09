package vision.genesis.android.mvp.presenters

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.terrakok.cicerone.Router
import vision.genesis.android.GenesisVisionApp
import vision.genesis.android.R
import vision.genesis.android.Screens
import vision.genesis.android.mvp.models.Constants
import vision.genesis.android.mvp.models.data.TraderInfo
import vision.genesis.android.mvp.models.domain.GetTradersListInteractor
import vision.genesis.android.mvp.views.TradersListView
import javax.inject.Inject

@InjectViewState
class TradersListPresenter: BasePresenter<TradersListView>() {

    @Inject
    lateinit var getTradersListInteractor: GetTradersListInteractor

    private var canLoadMore:Boolean = true

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var router: Router

    init {
        GenesisVisionApp.getAppComponent().inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showToolbar()
        viewState.onStartLoading()
        loadTradersList(0)
    }

    fun loadNextTraders(skip: Int) {
        if (canLoadMore) {
            viewState.showListProgress()
            loadTradersList(skip)
        }
    }

    fun refreshTraders() {
        canLoadMore = true

        viewState.showRefreshing()
        loadTradersList(0)
    }

    private fun loadTradersList(skip: Int) {
        val subscription = getTradersListInteractor.execute(skip)
            .subscribe({
                hideLoading()

                if (skip > 0) {
                    viewState.addTraders(it)
                } else {
                    viewState.setTraders(it)
                }

                if (it.size < Constants.TRADERS_LIST_PAGE_SIZE) {
                    // no more traders left
                    canLoadMore = false
                }

                viewState.hideError()
            }, {
                hideLoading()
                viewState.showError(it.message ?: context.getString(R.string.can_not_load_traders))
            })

        unsubscribeOnDestroy(subscription)
    }

    private fun hideLoading() {
        viewState.onFinishLoading()
        viewState.hideRefreshing()
        viewState.hideListProgress()
    }

    fun showTraderProfile(traderInfo: TraderInfo) {
        router.navigateTo(Screens.TRADER_PROFILE, traderInfo)
    }
}
