package vision.genesis.android.network.services

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import vision.genesis.android.mvp.models.data.PaymentInfo
import vision.genesis.android.mvp.models.data.TokenHolder
import vision.genesis.android.mvp.models.data.TraderGraphics
import vision.genesis.android.mvp.models.data.TraderInfo
import vision.genesis.android.network.ApiResponse
import vision.genesis.android.network.EtherScanApiResponse

interface TradersApi {
    @GET("getTradersList.php")
    fun getTradersList(@Query("skip") skip: Int): Observable<ApiResponse<List<TraderInfo>>>

    @GET("getTradersGraphics.php")
    fun getTraderGraphics(@Query("traderId") traderId: Long): Observable<ApiResponse<List<TraderGraphics>>>

    @GET("getTraderTokens.php")
    fun getTraderTokenHolders(@Query("traderId") traderId: Long): Observable<ApiResponse<List<TokenHolder>>>

    @GET("getPaymentInfo.php")
    fun getPaymentInfo(@Query("amount") amount: Float): Observable<ApiResponse<PaymentInfo>>

    @GET
    fun getGvtValue(@Url url: String,
                    @Query("address") address: String,
                    @Query("module") module: String = "account",
                    @Query("action") action: String = "tokenbalance",
                    @Query("contractaddress") contractAddress: String = "0x103c3A209da59d3E7C4A89307e66521e081CFDF0",
                    @Query("tag") tag: String = "latest",
                    @Query("apikey") apikey: String = "YourApiKeyToken"): Observable<EtherScanApiResponse>
}
