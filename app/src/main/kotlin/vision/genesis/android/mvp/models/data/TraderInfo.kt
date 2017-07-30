package vision.genesis.android.mvp.models.data

data class TraderInfo(val id: Long,
                      val name: String,
                      val country: String,
                      val currency: String,
                      val avatar: String,
                      val deposit: Int,
                      val trades: Int,
                      val weeks: Int,
                      val profit: Int,
                      val level: Int)
