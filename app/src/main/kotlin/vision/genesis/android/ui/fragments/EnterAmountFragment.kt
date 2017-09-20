package vision.genesis.android.ui.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_enter_amount.*
import kotlinx.android.synthetic.main.toolbar.*
import vision.genesis.android.R
import vision.genesis.android.mvp.models.data.TraderInfo
import vision.genesis.android.mvp.presenters.EnterAmountPresenter
import vision.genesis.android.mvp.presenters.TraderProfilePresenter
import vision.genesis.android.mvp.views.EnterAmountView


class EnterAmountFragment : MvpAppCompatFragment(), EnterAmountView {
    @InjectPresenter(type = PresenterType.LOCAL)
    lateinit var enterAmountPresenter: EnterAmountPresenter

    @ProvidePresenter(type = PresenterType.LOCAL)
    fun provideEnterAmountPresenter(): EnterAmountPresenter {
        val traderInfo = arguments?.getParcelable<TraderInfo>(TraderProfileFragment.TRADER_INFO_ARG)!!
        return EnterAmountPresenter(traderInfo)
    }

    companion object Factory {
        val TRADER_INFO_ARG = "trader"

        fun create(traderInfo: TraderInfo): EnterAmountFragment {
            val instance = EnterAmountFragment()
            val args = Bundle()
            args.putParcelable(TRADER_INFO_ARG, traderInfo)
            instance.arguments = args
            return instance
        }
    }

    private var maxAmountValue = 999999

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_enter_amount, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        amountView.requestFocus()
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(amountView, InputMethodManager.SHOW_IMPLICIT)

        limitAmountView()
    }

    private fun limitAmountView() {
        amountView.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun afterTextChanged(str: Editable?) {
                try {
                    val value = str?.toString()?.toInt()
                    if (value != null && value > maxAmountValue) {
                        amountView.setText(maxAmountValue.toString())
                    }
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }
            }
        })

    }

    override fun showTraderInfo(traderInfo: TraderInfo) {
        showToolbar(traderInfo.name)
    }

    override fun showUserInfo(availableTokens: Int, bidForToken: Float) {
        availableTokensView.text = availableTokens.toString()
        bidTokensView.text = bidForToken.toString()

        maxAmountValue = availableTokens
    }

    private fun showToolbar(title: String) {
        activity.title = title
        activity.toolbar.navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_arrow_back_black_24dp)
        activity.toolbar.setNavigationOnClickListener {
            enterAmountPresenter.goToBackScreen()
        }
    }
}