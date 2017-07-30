package vision.genesis.android.network

data class ApiResponse<T>(val status: String?,
                          val code: Int,
                          val result: T) {
    val isSuccess: Boolean
        get() = code == 200
}
