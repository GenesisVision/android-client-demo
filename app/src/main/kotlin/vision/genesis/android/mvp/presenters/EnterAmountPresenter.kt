package vision.genesis.android.mvp.presenters

import com.arellomobile.mvp.InjectViewState
import ru.terrakok.cicerone.Router
import vision.genesis.android.GenesisVisionApp
import vision.genesis.android.Screens
import vision.genesis.android.mvp.models.data.AccountInfo
import vision.genesis.android.mvp.models.data.PaymentArgs
import vision.genesis.android.mvp.models.data.TraderInfo
import vision.genesis.android.mvp.models.domain.AccountInteractor
import vision.genesis.android.mvp.views.EnterAmountView
import javax.inject.Inject

@InjectViewState
class EnterAmountPresenter(private val traderInfo: TraderInfo): BasePresenter<EnterAmountView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var accountInteractor: AccountInteractor

    init {
        GenesisVisionApp.getAppComponent().inject(this)
    }

    // TODO replace with real data, when api will be ready
    private val AVAILABLE_TOKENS = 15
    private val BID_FOR_TOKEN = 1.18f

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showTraderInfo(traderInfo)

        val account = accountInteractor.execute()
        viewState.showUserInfo(account.availableTokens, account.bidForOneToken)
    }

    fun goToBackScreen() {
        router.backTo(Screens.TRADER_PROFILE)
    }

    fun goToPaymentConfirmation(amountValue: Int) {
        val args = PaymentArgs(traderInfo, amountValue)
        router.navigateTo(Screens.PAYMENT_CONFIRMATION, args)
    }
}