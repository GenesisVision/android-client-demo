package vision.genesis.android.network


data class EtherScanApiResponse(val status: String?,
                          val message: String?,
                          val result: String) {
    val isSuccess: Boolean
        get() = message == "OK"
    val gvtValue: Float
        get() = result.substring(0, result.length - 16).toFloat() / 100f
}
