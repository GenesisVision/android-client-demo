package vision.genesis.android.ui.fragments

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.fragment_trader_profile.*
import kotlinx.android.synthetic.main.fragment_profile_info.*
import kotlinx.android.synthetic.main.fragment_profile_navigation.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.forEachChild
import org.jetbrains.anko.windowManager
import vision.genesis.android.R
import vision.genesis.android.mvp.models.data.TokenHolder
import vision.genesis.android.mvp.models.data.TraderGraphics
import vision.genesis.android.mvp.models.data.TraderInfo
import vision.genesis.android.mvp.presenters.TraderProfilePresenter
import vision.genesis.android.mvp.views.TraderProfileView
import vision.genesis.android.ui.adapters.TokenHoldersAdapter
import vision.genesis.android.ui.views.TokenHolderDecoration
import java.util.*


class TraderProfileFragment : MvpAppCompatFragment(), TraderProfileView {

    @InjectPresenter(type = PresenterType.LOCAL)
    lateinit var traderProfilePresenter: TraderProfilePresenter

    @ProvidePresenter(type = PresenterType.LOCAL)
    fun provideTraderProfilePresenter(): TraderProfilePresenter {
        val traderInfo = arguments?.getParcelable<TraderInfo>(TRADER_INFO_ARG)!!
        return TraderProfilePresenter(traderInfo)
    }

    private lateinit var holdersAdapter: TokenHoldersAdapter

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
        holdersAdapter = TokenHoldersAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_trader_profile, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loader.indeterminateDrawable.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary),
                android.graphics.PorterDuff.Mode.SRC_IN)

        traderGraphicsSelector.setOnClickListener {
            traderProfilePresenter.selectGraphics()
        }

        tokenHoldersSelector.setOnClickListener {
            traderProfilePresenter.selectTokenHolders()
        }

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        tokenHoldersList.layoutManager = linearLayoutManager
        tokenHoldersList.adapter = holdersAdapter

        val dividerSize = resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin)
        tokenHoldersList.addItemDecoration(TokenHolderDecoration(dividerSize))

        profitChartView.setNoDataText("")
    }

    override fun showTraderInfo(traderInfo: TraderInfo) {
        showToolbar(traderInfo.name)

        depositView.text = traderInfo.deposit.toString()
        fundView.text = traderInfo.fund.toString()
        profitView.text = traderInfo.profit.toString()

        fundTitleView.text = resources.getString(R.string.own_fund_currency, traderInfo.currency)
        depositTitleView.text = resources.getString(R.string.deposit_currency, traderInfo.currency)
        profitTitleView.text = resources.getString(R.string.profit_percent)

        daysLeftView.text = resources.getString(R.string.days_left, traderInfo.daysLeft)
    }

    override fun selectGraphics() {
        traderGraphicsSelector.isChecked = true
        tokenHoldersSelector.isChecked = false

        profitChartView.visibility = View.VISIBLE
        graphicsNavigationContainer.visibility = View.VISIBLE
        tokenHoldersList.visibility = View.GONE
    }

    override fun selectTokens() {
        traderGraphicsSelector.isChecked = false
        tokenHoldersSelector.isChecked = true

        profitChartView.visibility = View.GONE
        graphicsNavigationContainer.visibility = View.GONE
        tokenHoldersList.visibility = View.VISIBLE
    }

    override fun showTraderGraphics(graphics: List<TraderGraphics>) {
        val metrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(metrics)
        val width = (metrics.widthPixels
                - resources.getDimension(R.dimen.activity_horizontal_margin).toInt() * 2) / graphics.size
        val height = resources.getDimension(R.dimen.internal_navigation_button_height).toInt()

        var i: Int = 0
        while (i < graphics.size) {
            val button = createToggleButton(width, height, graphics[i].label)
            button.id = i + 1
            val index = i
            button.setOnClickListener {
                if (button.isChecked) {
                    traderProfilePresenter.showGraphic(index)
                } else {
                    button.isChecked = true
                }
            }
            graphicsNavigationContainer.addView(button)
            i++
        }

        setupChart()
    }

    private fun setupChart() {
        profitChartView.description.isEnabled = false
        profitChartView.setDrawGridBackground(false)
        profitChartView.isDragEnabled = false
        profitChartView.setTouchEnabled(false)
        profitChartView.xAxis.isEnabled = false
        profitChartView.axisLeft.isEnabled = false
        profitChartView.axisRight.isEnabled = false
        profitChartView.legend.isEnabled = false
        profitChartView.setDrawBorders(false)
        profitChartView.isAutoScaleMinMaxEnabled = false
    }

    private fun createToggleButton(width: Int, height: Int, label: String): ToggleButton {
        val btn = getLayoutInflater(null).inflate(R.layout.toggle_button_graphics, null) as ToggleButton
        btn.layoutParams = ViewGroup.LayoutParams(width, height)
        btn.textOn = label
        btn.textOff = label
        return btn
    }

    override fun showSelectedTraderGraphic(graphic: TraderGraphics, selectedItem: Int) {
        profitChartView.clear()
        graphicsNavigationContainer.forEachChild {
            (it as ToggleButton).isChecked = false
        }
        (graphicsNavigationContainer.getChildAt(selectedItem) as ToggleButton).isChecked = true

        val set = LineDataSet(getChartEntries(graphic.chartEntries), "")
        set.label = ""
        set.setDrawValues(false)
        set.lineWidth = 2f
        set.color = ContextCompat.getColor(context, R.color.colorAzure)
        set.setDrawCircles(false)

        val data = LineData(set)
        profitChartView.data = data
    }

    private fun getChartEntries(entries: List<Float>): List<Entry> {
        val values = ArrayList<Entry>()

        var i = 0f
        entries.forEach {
            values.add(Entry(i, it))
            i++
        }

        return values
    }

    override fun showTokenHolders(tokenHolders: List<TokenHolder>) {
        holdersAdapter.setHolders(tokenHolders)
    }

    private fun showToolbar(title: String) {
        activity.title = title
        activity.toolbar.navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_arrow_back_black_24dp)
        activity.toolbar.setNavigationOnClickListener {
            traderProfilePresenter.goToBackScreen()
        }
    }

    override fun hideLoading() {
        loader.visibility = View.GONE
    }

    override fun showError(message: String) {
        Snackbar.make(rootContainer, message, Snackbar.LENGTH_SHORT).show()
    }
}