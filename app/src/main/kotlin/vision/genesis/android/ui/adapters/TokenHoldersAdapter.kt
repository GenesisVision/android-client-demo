package vision.genesis.android.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_token_holder_item.view.*
import vision.genesis.android.R
import vision.genesis.android.extensions.loadUrl
import vision.genesis.android.mvp.models.data.TokenHolder
import java.util.ArrayList

class TokenHoldersAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val holders: MutableList<TokenHolder> = ArrayList()

    override fun getItemCount(): Int = holders.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return TokenHolderViewHolder((LayoutInflater.from(parent?.context).inflate(R.layout.list_token_holder_item, parent, false)))
    }

    fun setHolders(newHolders: List<TokenHolder>) {
        if (holders.size != 0) {
            val lastPosition = holders.size - 1
            holders.clear()
            notifyItemRangeRemoved(0, lastPosition)
        }
        holders.addAll(newHolders)
        notifyItemRangeInserted(0, newHolders.size - 1)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is TokenHolderViewHolder) {
            holder.bindHolder(holders[position])
        }
    }

    class TokenHolderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindHolder(holder: TokenHolder) {
            itemView.avatarView.loadUrl(holder.avatar)
            itemView.tokensNumberView.text = holder.tokensNumber.toString()
            itemView.plusBadge.text = "+" + holder.plus

            if (holder.isTop) {
                itemView.isTopBadge.visibility = View.VISIBLE
            } else {
                itemView.isTopBadge.visibility = View.GONE
            }
        }
    }
}