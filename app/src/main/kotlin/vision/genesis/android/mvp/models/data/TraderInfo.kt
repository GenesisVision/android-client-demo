package vision.genesis.android.mvp.models.data

import android.os.Parcel
import android.os.Parcelable

data class TraderInfo(val id: Long,
                      val name: String,
                      val country: String,
                      val currency: String,
                      val avatar: String,
                      val deposit: Int,
                      val daysLeft: Int,
                      val fund: Int,
                      val trades: Int,
                      val weeks: Int,
                      val chartEntries: List<Float>,
                      val profit: Int,
                      val level: Int) : Parcelable {

    constructor(`in`: Parcel): this(`in`.readLong(),
            `in`.readString(),
            `in`.readString(),
            `in`.readString(),
            `in`.readString(),
            `in`.readInt(),
            `in`.readInt(),
            `in`.readInt(),
            `in`.readInt(),
            `in`.readInt(),
            arrayListOf<Float>().apply {
                `in`.readList(this, Float::class.java.classLoader)
            },
            `in`.readInt(),
            `in`.readInt())

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(name)
        dest.writeString(country)
        dest.writeString(currency)
        dest.writeString(avatar)
        dest.writeInt(deposit)
        dest.writeInt(daysLeft)
        dest.writeInt(fund)
        dest.writeInt(trades)
        dest.writeInt(weeks)
        dest.writeList(chartEntries)
        dest.writeInt(profit)
        dest.writeInt(level)
    }

    override fun describeContents() = 0

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<TraderInfo> = object : Parcelable.Creator<TraderInfo> {

            override fun createFromParcel(`in`: Parcel) = TraderInfo(`in`)

            override fun newArray(size: Int) = arrayOfNulls<TraderInfo>(size)
        }
    }
}
