package com.app2m.github.network.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UsersOwner(var login: String = "",
                      val id: Long = 0,
                      var node_id: String = "",
                      var avatar_url:String = "") : Parcelable