package vision.genesis.android.network

data class EmptyApiResponse(val status: String,
                            val code: Int) {
    val isSuccess: Boolean
        get() = code == 200
}
