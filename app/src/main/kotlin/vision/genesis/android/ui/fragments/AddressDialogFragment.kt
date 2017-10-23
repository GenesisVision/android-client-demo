package vision.genesis.android.ui.fragments

import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import vision.genesis.android.R
import vision.genesis.android.mvp.presenters.TradersListPresenter


class AddressDialogFragment: AppCompatDialogFragment() {

    companion object Factory {
        fun create(presenter: TradersListPresenter): AddressDialogFragment {
            val instance = AddressDialogFragment()

            val args = Bundle()
            instance.arguments = args

            instance.presenter = presenter

            instance.setStyle(AppCompatDialogFragment.STYLE_NO_TITLE, 0)

            return instance
        }
    }

    private lateinit var presenter: TradersListPresenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_dialog_address, container, false)

        val addressField = view?.findViewById(R.id.addressField) as EditText?
        addressField?.setText(presenter.getAddress())

        val okBtn = view?.findViewById(R.id.okBtn) as Button?
        okBtn?.setOnClickListener {
            if (!TextUtils.isEmpty(addressField?.text)) {
                presenter.loadGvtValue(addressField!!.text.toString())
            }
            dismiss()
        }

        presenter.showDialogAddress()


        return view
    }

}