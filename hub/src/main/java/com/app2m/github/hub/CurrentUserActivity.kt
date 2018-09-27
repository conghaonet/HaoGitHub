package com.app2m.github.hub

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.app2m.github.hub.base.BaseActivity
import com.app2m.github.hub.databinding.ActivityCurrentUserBinding
import com.app2m.github.hub.vm.CurrentUserVM
import com.app2m.github.network.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast

class CurrentUserActivity : BaseActivity(), AnkoLogger {
    private val mBinding by lazy {
        DataBindingUtil.setContentView<ActivityCurrentUserBinding>(this, R.layout.activity_current_user)
    }
    private val mApiService = RequestClient.buildService(GitHubService::class.java)
    private val mCompositeDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var disposable = mApiService.getCurrentUser().schedule().subscribeBy (
                onNext = {
                    mBinding.model = CurrentUserVM(it)
                },onError = {
                    toast(it.message!!)
        })
        mCompositeDisposable.add(disposable)

    }
}
