package vision.genesis.android.mvp.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import vision.genesis.android.GenesisVisionApp
import vision.genesis.android.mvp.models.Constants
import vision.genesis.android.mvp.models.domain.GetTradersListInteractor
import vision.genesis.android.mvp.views.TradersListView
import javax.inject.Inject

@InjectViewState
class TradersListPresenter: BasePresenter<TradersListView>() {

    @Inject
    lateinit var getTradersListInteractor: GetTradersListInteractor

    private var canLoadMore:Boolean = true

    init {
        GenesisVisionApp.getAppComponent().inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
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
                viewState.showError(it.message ?: "Can not load traders list")
            })

        unsubscribeOnDestroy(subscription)
    }

    private fun hideLoading() {
        viewState.onFinishLoading()
        viewState.hideRefreshing()
        viewState.hideListProgress()
    }
}
