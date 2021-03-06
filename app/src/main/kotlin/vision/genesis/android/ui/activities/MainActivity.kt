package vision.genesis.android.ui.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import com.arellomobile.mvp.MvpAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.toast
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.SupportFragmentNavigator
import ru.terrakok.cicerone.commands.Back
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import ru.terrakok.cicerone.commands.Replace
import vision.genesis.android.GenesisVisionApp
import vision.genesis.android.R
import vision.genesis.android.Screens
import vision.genesis.android.mvp.models.data.PaymentArgs
import vision.genesis.android.mvp.models.data.TraderInfo
import vision.genesis.android.network.services.TradersService
import vision.genesis.android.ui.fragments.EnterAmountFragment
import vision.genesis.android.ui.fragments.PaymentConfirmationFragment
import vision.genesis.android.ui.fragments.TraderProfileFragment
import vision.genesis.android.ui.fragments.TradersListFragment
import javax.inject.Inject

class MainActivity : MvpAppCompatActivity() {
    @Inject
    lateinit var navigatorHolder:NavigatorHolder

    @Inject
    lateinit var tradersService: TradersService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GenesisVisionApp.getAppComponent().inject(this)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            navigator.applyCommand(Replace(Screens.TRADERS_LIST, 1))
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    private val navigator: Navigator = object: SupportFragmentNavigator(supportFragmentManager, R.id.mainContainer) {
        override fun createFragment(screenKey: String?, data: Any?): Fragment? {
            if (screenKey == Screens.TRADERS_LIST) {
                return TradersListFragment.create()
            } else if (screenKey == Screens.TRADER_PROFILE) {
                return TraderProfileFragment.create(data as TraderInfo)
            } else if (screenKey == Screens.ENTER_AMOUNT) {
                return EnterAmountFragment.create(data as TraderInfo)
            } else if (screenKey == Screens.PAYMENT_CONFIRMATION) {
                val args = data as PaymentArgs
                return PaymentConfirmationFragment.create(args.traderInfo, args.amountValue)
            } else {
                return null
            }
        }

        override fun exit() {
            finish()
        }

        override fun showSystemMessage(message: String?) {
            message?.let {
                toast(it)
            }
        }
    }
}
