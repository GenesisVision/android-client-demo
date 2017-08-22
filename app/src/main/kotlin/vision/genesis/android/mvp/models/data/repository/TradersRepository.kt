package vision.genesis.android.mvp.models.data.repository

import io.reactivex.Observable
import ru.terrakok.cicerone.NavigatorHolder
import vision.genesis.android.mvp.models.data.TraderGraphics
import vision.genesis.android.mvp.models.data.TraderInfo
import javax.inject.Inject

interface TradersRepository {
    fun getTraderList(skip: Int): Observable<List<TraderInfo>>
    fun getTraderGraphics(traderId: Long): Observable<List<TraderGraphics>>
}
