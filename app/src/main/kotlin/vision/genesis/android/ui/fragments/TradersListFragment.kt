package vision.genesis.android.ui.fragments

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_traders_list.*
import vision.genesis.android.R
import vision.genesis.android.mvp.models.Constants
import vision.genesis.android.mvp.models.data.TraderInfo
import vision.genesis.android.mvp.presenters.TradersListPresenter
import vision.genesis.android.mvp.views.TradersListView
import vision.genesis.android.ui.adapters.TradersListAdapter

class TradersListFragment : MvpAppCompatFragment(), TradersListView {

    @InjectPresenter(type = PresenterType.GLOBAL)
    lateinit var tradersListPresenter: TradersListPresenter

    @ProvidePresenter(type = PresenterType.GLOBAL)
    fun providerTradersListPresenter() = TradersListPresenter()

    private var adapter: TradersListAdapter = TradersListAdapter()

    private var loadingNewTraders: Boolean = false

    companion object Factory {
        fun create(): TradersListFragment = TradersListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_traders_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loader.indeterminateDrawable.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary),
                android.graphics.PorterDuff.Mode.SRC_IN)

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
}