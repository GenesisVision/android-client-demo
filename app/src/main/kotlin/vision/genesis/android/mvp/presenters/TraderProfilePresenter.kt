package vision.genesis.android.mvp.presenters

import com.arellomobile.mvp.InjectViewState
import ru.terrakok.cicerone.Router
import vision.genesis.android.GenesisVisionApp
import vision.genesis.android.Screens
import vision.genesis.android.mvp.models.data.TraderInfo
import vision.genesis.android.mvp.views.TraderProfileView
import javax.inject.Inject

@InjectViewState
class TraderProfilePresenter(val traderInfo: TraderInfo): BasePresenter<TraderProfileView>() {
    @Inject
    lateinit var router: Router

    init {
        GenesisVisionApp.getAppComponent().inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showTraderInfo(traderInfo)
    }

    fun goToBackScreen() {
        router.backTo(Screens.TRADERS_LIST)
    }
}
