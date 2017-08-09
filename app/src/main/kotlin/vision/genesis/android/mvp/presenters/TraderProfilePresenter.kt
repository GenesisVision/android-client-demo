package vision.genesis.android.mvp.presenters

import com.arellomobile.mvp.InjectViewState
import ru.terrakok.cicerone.Router
import vision.genesis.android.GenesisVisionApp
import vision.genesis.android.Screens
import vision.genesis.android.mvp.views.TraderProfileView
import javax.inject.Inject

@InjectViewState
class TraderProfilePresenter: BasePresenter<TraderProfileView>() {
    @Inject
    lateinit var router: Router

    init {
        GenesisVisionApp.getAppComponent().inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showToolbar()
    }

    fun goToBackScreen() {
        router.backTo(Screens.TRADERS_LIST)
    }
}
