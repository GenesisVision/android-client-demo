package vision.genesis.android.mvp.models.data


data class AccountInfo(val name: String,
                      val avatar: String,
                      val key: String,
                      val availableTokens: Int,
                      val bidForOneToken: Float)