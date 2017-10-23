package vision.genesis.android.ui.fragments

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.view.menu.MenuBuilder
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_traders_list.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar_plus.*
import vision.genesis.android.R
import vision.genesis.android.mvp.models.Constants
import vision.genesis.android.mvp.models.data.TraderInfo
import vision.genesis.android.mvp.presenters.TradersListPresenter
import vision.genesis.android.mvp.views.TradersListView
import vision.genesis.android.ui.adapters.TradersListAdapter
import android.content.Intent
import android.net.Uri


class TradersListFragment : MvpAppCompatFragment(), TradersListView {

    @InjectPresenter(type = PresenterType.GLOBAL)
    lateinit var tradersListPresenter: TradersListPresenter

    private lateinit var adapter: TradersListAdapter

    private var loadingNewTraders: Boolean = false

    private var menu: Menu? = null

    private var gvtValue: Float = 0.01f

    private var dialog: AddressDialogFragment? = null

    companion object Factory {
        fun create(): TradersListFragment = TradersListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = TradersListAdapter(tradersListPresenter)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_traders_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loader.indeterminateDrawable.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary),
                android.graphics.PorterDuff.Mode.SRC_IN)
        refreshLayout.setColorSchemeResources(R.color.colorPrimary)

        val linearLayoutManager = LinearLayoutManager(context)
        tradersList.layoutManager = linearLayoutManager

        tradersList.adapter = adapter

        refreshLayout.setOnRefreshListener {
            tradersListPresenter.refreshTraders()
        }

        tradersList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                recyclerView?.post({
                    val visibleThreshold = 2
                    val totalItemCount = linearLayoutManager.itemCount
                    val lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                    if (!loadingNewTraders && totalItemCount <= (lastVisibleItem + visibleThreshold)
                            && !refreshLayout.isRefreshing && totalItemCount >= Constants.TRADERS_LIST_PAGE_SIZE) {
                        tradersListPresenter.loadNextTraders(totalItemCount)
                    }
                })
            }
        })
    }

    override fun checkAddressDialog() {
        if (tradersListPresenter.needAddressDialog()) {
            showAddressDialog()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        this.menu = menu
        inflater?.inflate(R.menu.menu_traders_list, menu)
        showGvtValue(gvtValue)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_add_tokens) {
            val url = "https://genesis.vision"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        } else if (item?.itemId == R.id.action_change_address) {
            showAddressDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showToolbar() {
        activity.title = getString(R.string.traders)
        activity.toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.colorFontDark))
        activity.toolbar.navigationIcon = null
        activity.toolbar.menu
        activity.toolbar.title
    }

    override fun setTraders(traders: List<TraderInfo>) {
        adapter.setTraders(traders)
        tradersList.scrollToPosition(0)
    }

    override fun addTraders(traders: List<TraderInfo>) {
        adapter.addTraders(traders)
    }

    override fun onStartLoading() {
        loader.visibility = View.VISIBLE
    }

    override fun onFinishLoading() {
        loader.visibility = View.GONE
    }

    override fun showError(message: String) {
        Snackbar.make(rootContainer, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun hideError() {

    }

    override fun showGvtValue(gvt: Float) {
        gvtValue = gvt

        val menuItem = menu?.findItem(R.id.action_add_tokens)
        val view = menuItem?.actionView
        val textView: TextView? = view?.findViewById(R.id.plusToolbarText) as TextView?
        textView?.text = gvt.toString() + " GVT"

        view?.setOnClickListener {
            onOptionsItemSelected(menuItem)
        }
    }

    override fun showListProgress() {
        loadingNewTraders = true
        adapter.showListProgress()
    }

    override fun hideListProgress() {
        loadingNewTraders = false
        adapter.hideListProgress()
    }

    override fun showRefreshing() {
        refreshLayout.isRefreshing = true
    }

    override fun hideRefreshing() {
        refreshLayout.isRefreshing = false
    }

    private fun showAddressDialog() {
        dialog = AddressDialogFragment.create(tradersListPresenter)
        dialog?.show(activity.supportFragmentManager, dialog?.tag)
    }

    override fun onPause() {
        dialog?.dismiss()
        super.onPause()
    }
}