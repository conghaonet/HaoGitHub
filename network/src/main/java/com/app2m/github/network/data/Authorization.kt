package com.app2m.github.network.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Authorization(var id: Long, var url: String,
                         var app: App, var token: String,
                         var hashed_token: String, var token_last_eight: String,
                         var note: String, var note_url: String,
                         var created_at: String, var updated_at: String,

                         var scopes : List<String>, var fingerprint: String) : Parcelable {
    @Parcelize
    data class App(var client_id: String, var url: String, var name: String) : Parcelable
}