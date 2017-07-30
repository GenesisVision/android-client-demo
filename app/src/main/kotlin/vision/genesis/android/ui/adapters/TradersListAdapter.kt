package vision.genesis.android.ui.adapters

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.list_traders_list_item.view.*
import vision.genesis.android.R
import vision.genesis.android.extensions.loadUrl
import vision.genesis.android.mvp.models.data.TraderInfo
import java.util.*

class TradersListAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val VIEW_ITEM = 1
    val VIEW_PROG = 0

    val traders: MutableList<TraderInfo> = ArrayList()

    var showProgress: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
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
            holder.bindTrader(traders[position])
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
        val lastPosition = traders.size - 1
        traders.addAll(newTraders)
        notifyItemRangeInserted(lastPosition, traders.size - 1)
    }

    override fun getItemViewType(position: Int): Int {
        if (position == traders.size && showProgress) {
            return VIEW_PROG
        } else {
            return VIEW_ITEM
        }
    }

    class TraderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindTrader(trader: TraderInfo) {
            itemView.avatar.loadUrl(trader.avatar)
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
