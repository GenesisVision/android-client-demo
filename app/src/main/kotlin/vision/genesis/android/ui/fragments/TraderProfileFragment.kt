package vision.genesis.android.ui.fragments

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_trader_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import vision.genesis.android.R
import vision.genesis.android.mvp.models.data.TraderInfo
import vision.genesis.android.mvp.presenters.TraderProfilePresenter
import vision.genesis.android.mvp.views.TraderProfileView


class TraderProfileFragment : MvpAppCompatFragment(), TraderProfileView {

    @InjectPresenter(type = PresenterType.LOCAL)
    lateinit var traderProfilePresenter: TraderProfilePresenter

    @ProvidePresenter(type = PresenterType.LOCAL)
    fun provideTraderProfilePresenter(): TraderProfilePresenter {
        val traderInfo = arguments?.getParcelable<TraderInfo>(TRADER_INFO_ARG)!!
        return TraderProfilePresenter(traderInfo)
    }

    companion object Factory {
        val TRADER_INFO_ARG = "trader"

        fun create(traderInfo: TraderInfo): TraderProfileFragment {
            val instance = TraderProfileFragment()
            val args = Bundle()
            args.putParcelable(TRADER_INFO_ARG, traderInfo)
            instance.arguments = args
            return instance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_trader_profile, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loader.indeterminateDrawable.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary),
                android.graphics.PorterDuff.Mode.SRC_IN)
    }

    override fun showTraderInfo(traderInfo: TraderInfo) {
        showToolbar(traderInfo.name)

        depositView.text = traderInfo.deposit.toString()
        fundView.text = traderInfo.fund.toString()
        profitView.text = traderInfo.profit.toString()

        daysLeftView.text = resources.getString(R.string.days_left, traderInfo.daysLeft)
    }

    private fun showToolbar(title: String) {
        activity.title = title
        activity.toolbar.navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_arrow_back_black_24dp)
        activity.toolbar.setNavigationOnClickListener {
            traderProfilePresenter.goToBackScreen()
        }
    }
}