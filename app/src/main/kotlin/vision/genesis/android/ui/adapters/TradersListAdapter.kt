package vision.genesis.android.ui.adapters

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.list_traders_list_item.view.*
import vision.genesis.android.R
import vision.genesis.android.extensions.loadUrl
import vision.genesis.android.mvp.models.data.TraderInfo
import vision.genesis.android.mvp.presenters.TradersListPresenter
import java.util.*

class TradersListAdapter(val parentPresenter: TradersListPresenter): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_ITEM = 1
    private val VIEW_PROG = 0

    private val traders: MutableList<TraderInfo> = ArrayList()

    private var showProgress: Boolean = false

    private var maxTitleWidth: Int = 9999
    private var infoContainerWidth: Int = 300

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        if (parent != null) {
            maxTitleWidth = parent.measuredWidth - parent.context.resources.getDimension(R.dimen.traders_item_title_max_width_diff).toInt()
            infoContainerWidth = parent.measuredWidth / 4
        }
        var vh: RecyclerView.ViewHolder? = null
        if (viewType == VIEW_ITEM) {
            vh = TraderViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.list_traders_list_item, parent, false))
        } else if (viewType == VIEW_PROG) {
            vh = ProgressViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.list_progress_item, parent, false))
        }
        return vh
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is TraderViewHolder) {
            holder.bindTrader(traders[position], maxTitleWidth, infoContainerWidth, parentPresenter)
        }
    }

    override fun getItemCount(): Int {
        var lastElement = 0
        if (showProgress) {
            lastElement ++
        }
        return traders.size + lastElement
    }

    fun setTraders(newTraders: List<TraderInfo>) {
        if (traders.size != 0) {
            val lastPosition = traders.size - 1
            traders.clear()
            notifyItemRangeRemoved(0, lastPosition)
        }
        traders.addAll(newTraders)
        notifyItemRangeInserted(0, newTraders.size - 1)
    }

    fun addTraders(newTraders: List<TraderInfo>) {
        val lastPosition = itemCount
        traders.addAll(newTraders)
        notifyItemRangeInserted(lastPosition, itemCount - lastPosition)
    }

    fun showListProgress() {
        if (!showProgress) {
            showProgress = true
            notifyItemInserted(itemCount)
        }
    }

    fun hideListProgress() {
        if (showProgress) {
            showProgress = false
            notifyItemRemoved(itemCount)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == traders.size && showProgress) {
            return VIEW_PROG
        } else {
            return VIEW_ITEM
        }
    }

    class TraderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindTrader(trader: TraderInfo, maxTitleWidth: Int, infoContainerWidth: Int, parentPresenter: TradersListPresenter) {
            itemView.avatarView.loadUrl(trader.avatar)
            itemView.levelView.text = trader.level.toString()
            itemView.nameView.text = trader.name
            itemView.depositView.text = trader.deposit.toString() + " " + trader.currency
            itemView.tradesView.text = trader.trades.toString()
            itemView.weeksView.text = trader.weeks.toString()
            itemView.currencyView.text = trader.currency
            itemView.profitView.text = trader.profit.toString() + "%"

            setChartData(getEntries(trader))

            itemView.setOnClickListener {
                parentPresenter.showTraderProfile(trader)
            }

            setLayoutSizes(maxTitleWidth, infoContainerWidth)
        }

        private fun setChartData(entries: List<Entry>) {
            val set = LineDataSet(entries, "")
            set.label = ""
            set.setDrawValues(false)
            set.lineWidth = 2f
            set.color = ContextCompat.getColor(itemView.context, R.color.colorAzure)
            set.setDrawCircles(false)
            val data = LineData(set)
            itemView.profitChartView.data = data

            itemView.profitChartView.description.isEnabled = false
            itemView.profitChartView.setDrawGridBackground(false)
            itemView.profitChartView.isDragEnabled = false
            itemView.profitChartView.setTouchEnabled(false)
            itemView.profitChartView.xAxis.isEnabled = false
            itemView.profitChartView.axisLeft.isEnabled = false
            itemView.profitChartView.axisRight.isEnabled = false
            itemView.profitChartView.legend.isEnabled = false
            itemView.profitChartView.setDrawBorders(false)
            itemView.profitChartView.isAutoScaleMinMaxEnabled = false

        }

        private fun getEntries(traderInfo: TraderInfo): List<Entry> {
            val values = ArrayList<Entry>()

            var i = 0f
            traderInfo.chartEntries.forEach {
                values.add(Entry(i, it))
                i++
            }

            return values
        }

        private fun setLayoutSizes(maxTitleWidth: Int, infoContainerWidth: Int) {
            itemView.nameView.maxWidth = maxTitleWidth

            itemView.depositInfoContainer.layoutParams.width = infoContainerWidth
            itemView.tradesInfoContainer.layoutParams.width = infoContainerWidth
            itemView.weekInfoContainer.layoutParams.width = infoContainerWidth
            itemView.profitInfoContainer.layoutParams.width = infoContainerWidth
        }
    }

    class ProgressViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            val loader = view.findViewById(R.id.loader) as ProgressBar
            loader.indeterminateDrawable.setColorFilter(ContextCompat.getColor(view.context, R.color.colorPrimary),
                    android.graphics.PorterDuff.Mode.SRC_IN)
        }
    }
}
