package com.app2m.github.hub.vm

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.app2m.github.network.data.User

class CurrentUserVM(private val user: User) : BaseObservable() {

    @Bindable
    fun getHomePage() : String {
        return user.html_url
    }

}