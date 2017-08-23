package vision.genesis.android.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import vision.genesis.android.R

fun ImageView.loadUrl(url: String) = Glide.with(context.applicationContext).load(url).error(R.mipmap.logo).into(this)
