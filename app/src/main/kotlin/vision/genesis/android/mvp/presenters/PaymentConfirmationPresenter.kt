package vision.genesis.android.mvp.presenters

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import ru.terrakok.cicerone.Router
import vision.genesis.android.GenesisVisionApp
import vision.genesis.android.R
import vision.genesis.android.Screens
import vision.genesis.android.mvp.models.data.TraderInfo
import vision.genesis.android.mvp.models.domain.AccountInteractor
import vision.genesis.android.mvp.models.domain.GetPaymentInfoInteractor
import vision.genesis.android.mvp.views.PaymentConfirmationView
import javax.inject.Inject

@InjectViewState
class PaymentConfirmationPresenter(private val traderInfo: TraderInfo,
                                   private val tokenAmount: Int): BasePresenter<PaymentConfirmationView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var getPaymentInfoInteractor: GetPaymentInfoInteractor

    @Inject
    lateinit var accountInteractor: AccountInteractor

    init {
        GenesisVisionApp.getAppComponent().inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showTraderInfo(traderInfo)
        viewState.showAccountInfo(accountInteractor.execute())
        loadPaymentInfo()
    }

    fun goToBackScreen() {
        router.backTo(Screens.ENTER_AMOUNT)
    }

    fun goToStartScreen() {
        router.backTo(Screens.TRADERS_LIST)
    }

    private fun loadPaymentInfo() {
        val errorDefaultMessage = context.getString(R.string.can_not_load_trader_graphics)
        val subscription = getPaymentInfoInteractor.execute(tokenAmount)
                .subscribe({
                    viewState.showPaymentInfo(it)
                }, {
                    viewState.showError(it.message ?: errorDefaultMessage)
                })
        unsubscribeOnDestroy(subscription)
    }
}