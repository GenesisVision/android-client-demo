package vision.genesis.android.ui.fragments

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_payment_confirmation.*
import kotlinx.android.synthetic.main.fragment_payment_info.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.toast
import vision.genesis.android.R
import vision.genesis.android.extensions.loadUrl
import vision.genesis.android.mvp.models.data.AccountInfo
import vision.genesis.android.mvp.models.data.PaymentInfo
import vision.genesis.android.mvp.models.data.TraderInfo
import vision.genesis.android.mvp.presenters.PaymentConfirmationPresenter
import vision.genesis.android.mvp.views.PaymentConfirmationView


class PaymentConfirmationFragment: MvpAppCompatFragment(), PaymentConfirmationView {
    @InjectPresenter(type = PresenterType.LOCAL)
    lateinit var paymentPresenter: PaymentConfirmationPresenter

    @ProvidePresenter(type = PresenterType.LOCAL)
    fun providePaymentPresenter(): PaymentConfirmationPresenter {
        val traderInfo = arguments?.getParcelable<TraderInfo>(PaymentConfirmationFragment.TRADER_INFO_ARG)!!
        val amountValue = arguments?.getFloat(PaymentConfirmationFragment.AMOUNT_VALUE)!!
        return PaymentConfirmationPresenter(traderInfo, amountValue)
    }

    companion object Factory {
        val TRADER_INFO_ARG = "trader"
        val AMOUNT_VALUE = "amountValue"

        fun create(traderInfo: TraderInfo, amountValue: Float): PaymentConfirmationFragment {
            val instance = PaymentConfirmationFragment()
            val args = Bundle()
            args.putParcelable(TRADER_INFO_ARG, traderInfo)
            args.putFloat(AMOUNT_VALUE, amountValue)
            instance.arguments = args
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_payment_confirmation, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showToolbar()

        rejectBtn.setOnClickListener {
            paymentPresenter.goToBackScreen()
        }

        confirmBtn.setOnClickListener {
            activity.toast(getString(R.string.complete))
            paymentPresenter.goToStartScreen()
        }
    }

    override fun showTraderInfo(traderInfo: TraderInfo) {
        showToolbar()

        traderAvatarView.loadUrl(traderInfo.avatar)
        traderNameView.text = traderInfo.name
    }

    override fun showAccountInfo(info: AccountInfo) {
        accountAvatarView.loadUrl(info.avatar)
        accountNameView.text = info.name
        accountDescriptionView.text = info.key
        // TODO remove this label. Temp solution for equal height
        traderDescriptionView.text = info.key
    }

    override fun showPaymentInfo(info: PaymentInfo) {
        amountValueView.text = info.amount.toString() + " GVT"
        amountValueUSDView.text = info.amountUSD.toString() + " USD"
        amountFeeView.text = info.fee.toString() + " GVT"
        amountFeeUSDView.text = info.feeUSD.toString() + " USD"
        amountTotalView.text = (info.amount + info.fee).toString() + " GVT"
        amountTotalUSDView.text = (info.amountUSD + info.feeUSD).toString() + " USD"
    }

    override fun showError(error: String) {
        Snackbar.make(rootContainer, error, Snackbar.LENGTH_SHORT).show()
    }

    private fun showToolbar() {
        activity.title = getString(R.string.invest)
        activity.toolbar.navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_arrow_back_black_24dp)
        activity.toolbar.setNavigationOnClickListener {
            paymentPresenter.goToBackScreen()
        }
    }

}