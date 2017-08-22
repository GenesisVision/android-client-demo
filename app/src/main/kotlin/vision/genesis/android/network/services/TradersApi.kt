package vision.genesis.android.network.services

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import vision.genesis.android.mvp.models.data.TraderGraphics
import vision.genesis.android.mvp.models.data.TraderInfo
import vision.genesis.android.network.ApiResponse

interface TradersApi {
    @GET("getTradersList.php")
    fun getTradersList(@Query("skip") skip: Int): Observable<ApiResponse<List<TraderInfo>>>

    @GET("getTradersGraphics.php")
    fun getTraderGraphics(@Query("traderId") traderId: Long): Observable<ApiResponse<List<TraderGraphics>>>
}
