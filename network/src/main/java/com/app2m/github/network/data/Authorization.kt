package com.app2m.github.network.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Authorization(var id: Long, var url: String, var scopes : List<String>, var token: String) : Parcelable