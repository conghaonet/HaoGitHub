package com.app2m.github.network.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ErrResponse(var responseCode: Int, var responseMsg : String, var body: Body?) : Parcelable {
    @Parcelize
    data class Body(@SerializedName("documentation_url") var docUrl: String, var message: String): Parcelable
}