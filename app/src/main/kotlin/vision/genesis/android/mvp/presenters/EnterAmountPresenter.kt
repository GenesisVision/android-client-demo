package vision.genesis.android.mvp.presenters

import com.arellomobile.mvp.InjectViewState
import ru.terrakok.cicerone.Router
import vision.genesis.android.GenesisVisionApp
import vision.genesis.android.Screens
import vision.genesis.android.mvp.models.data.TraderInfo
import vision.genesis.android.mvp.views.EnterAmountView
import javax.inject.Inject

@InjectViewState
class EnterAmountPresenter(private val traderInfo: TraderInfo): BasePresenter<EnterAmountView>() {
    @Inject
    lateinit var router: Router

    init {
        GenesisVisionApp.getAppComponent().inject(this)
    }

    // TODO replace with real data, when api will be ready
    private val AVAILABLE_TOKENS = 15
    private val BID_FOR_TOKEN = 1.18f

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showTraderInfo(traderInfo)
        viewState.showUserInfo(AVAILABLE_TOKENS, BID_FOR_TOKEN)
    }

    fun goToBackScreen() {
        router.backTo(Screens.TRADER_PROFILE)
    }
}