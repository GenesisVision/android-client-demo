package vision.genesis.android.mvp.models.data


data class AccountInfo(var name: String,
                      var avatar: String,
                      var key: String,
                      var availableTokens: Float,
                      var bidForOneToken: Float)