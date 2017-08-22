package vision.genesis.android.mvp.presenters

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import ru.terrakok.cicerone.Router
import vision.genesis.android.GenesisVisionApp
import vision.genesis.android.R
import vision.genesis.android.Screens
import vision.genesis.android.mvp.models.data.TraderGraphics
import vision.genesis.android.mvp.models.data.TraderInfo
import vision.genesis.android.mvp.models.domain.GetTraderGraphicsInteractor
import vision.genesis.android.mvp.views.TraderProfileView
import javax.inject.Inject

@InjectViewState
class TraderProfilePresenter(val traderInfo: TraderInfo): BasePresenter<TraderProfileView>() {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var getTraderGraphicsInteractor: GetTraderGraphicsInteractor

    private var selectedGraphicIndex: Int = 0

    @Inject
    lateinit var context: Context

    private var graphics: List<TraderGraphics>? = null

    init {
        GenesisVisionApp.getAppComponent().inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showTraderInfo(traderInfo)
        loadTraderGraphics()
    }

    fun goToBackScreen() {
        router.backTo(Screens.TRADERS_LIST)
    }

    fun showGraphic(index: Int) {
        if (graphics != null && graphics!!.size > index) {
            viewState.showSelectedTraderGraphic(graphics!![index], index)
        }
    }

    private fun loadTraderGraphics() {
        val errorDefaultMessage = context.getString(R.string.can_not_load_trader_graphics)
        val subscription = getTraderGraphicsInteractor.execute(traderInfo.id)
            .subscribe({
                graphics = it
                viewState.hideLoading()
                viewState.showTraderGraphics(it)
                selectedGraphicIndex = 0

                if (it.size > 0) {
                    viewState.showSelectedTraderGraphic(it[0], 0)
                }
            }, {
                viewState.hideLoading()
                viewState.showError(it.message ?: errorDefaultMessage)
            })
        unsubscribeOnDestroy(subscription)
    }
}
