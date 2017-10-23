package vision.genesis.android.ui.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
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
import vision.genesis.android.mvp.views.EnterAmountView


class EnterAmountFragment : MvpAppCompatFragment(), EnterAmountView {
    @InjectPresenter(type = PresenterType.LOCAL)
    lateinit var enterAmountPresenter: EnterAmountPresenter

    @ProvidePresenter(type = PresenterType.LOCAL)
    fun provideEnterAmountPresenter(): EnterAmountPresenter {
        val traderInfo = arguments?.getParcelable<TraderInfo>(EnterAmountFragment.TRADER_INFO_ARG)!!
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

    private var maxAmountValue: Float = 999999f

    private var lastAmountValue: Float = 0f

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_enter_amount, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        amountView.isFocusableInTouchMode = true
        amountView.requestFocus()
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(amountView, InputMethodManager.SHOW_IMPLICIT)

        limitAmountView()

        buyView.setOnClickListener{
            buyTokens()
        }

        amountView.setOnEditorActionListener { textView, actionId, keyEvent ->
            if ((keyEvent != null && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                buyTokens()
            }
            false
        }
    }

    override fun onPause() {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(amountView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        super.onPause()
    }

    private fun buyTokens() {
        if (lastAmountValue > 0) {
            enterAmountPresenter.goToPaymentConfirmation(lastAmountValue)
        }
    }

    private fun limitAmountView() {
        amountView.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun afterTextChanged(str: Editable?) {
                try {
                    val value = str?.toString()?.toFloat()
                    value?.let {
                        if (it > maxAmountValue) {
                            amountView.setText(maxAmountValue.toString())
                            lastAmountValue = maxAmountValue
                        } else {
                            lastAmountValue = it
                        }
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

    override fun showUserInfo(availableTokens: Float, bidForToken: Float) {
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