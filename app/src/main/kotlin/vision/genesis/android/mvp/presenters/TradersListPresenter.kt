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
import vision.genesis.android.mvp.models.domain.AccountInteractor
import vision.genesis.android.mvp.models.domain.GetGvtValueInteractor
import vision.genesis.android.mvp.models.domain.GetTradersListInteractor
import vision.genesis.android.mvp.views.TradersListView
import javax.inject.Inject

@InjectViewState
class TradersListPresenter: BasePresenter<TradersListView>() {

    @Inject
    lateinit var getTradersListInteractor: GetTradersListInteractor

    @Inject
    lateinit var getGvtValueInteractor: GetGvtValueInteractor

    private var canLoadMore:Boolean = true

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var router: Router

    private var isAddressDialogShown: Boolean = false

    init {
        GenesisVisionApp.getAppComponent().inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showToolbar()
        viewState.onStartLoading()
        loadTradersList(0)

        AccountInteractor.loadAddress(context)
        if (!AccountInteractor.isAddressDefault()) {
            loadGvtValue(AccountInteractor.account.key)
        }
        viewState.checkAddressDialog()
    }

    fun loadNextTraders(skip: Int) {
        if (canLoadMore) {
            viewState.showListProgress()
            loadTradersList(skip)
        }
    }

    fun needAddressDialog(): Boolean {
        return AccountInteractor.isAddressDefault() && !isAddressDialogShown
    }

    fun showDialogAddress() {
        isAddressDialogShown = true
    }

    fun getAddress(): String {
        if (AccountInteractor.isAddressDefault()) {
            return ""
        } else {
            return AccountInteractor.account.key
        }
    }

    fun refreshTraders() {
        canLoadMore = true

        viewState.showRefreshing()
        loadTradersList(0)
    }

    fun loadGvtValue(address: String) {
        AccountInteractor.saveAddress(context, address)
        val errorDefaultMessage = context.getString(R.string.can_not_load_gvt)
        val subscription = getGvtValueInteractor.execute(address)
                .subscribe({
                    viewState.showGvtValue(it)
                    AccountInteractor.saveTokens(it)
                }, {
                    viewState.showError(errorDefaultMessage)
                    viewState.showGvtValue(5f)
                })

        unsubscribeOnDestroy(subscription)
    }

    private fun loadTradersList(skip: Int) {
        val errorDefaultMessage = context.getString(R.string.can_not_load_traders)
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
                viewState.showError(it.message ?: errorDefaultMessage)
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
