package com.app2m.github.hub

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.text.InputType
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.app2m.github.hub.base.BaseActivity
import com.app2m.github.hub.databinding.LoginActivityBinding
import com.app2m.github.network.*
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.Credentials
import org.jetbrains.anko.*
import org.jetbrains.anko.design.textInputEditText
import org.jetbrains.anko.design.textInputLayout
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import retrofit2.HttpException
/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : BaseActivity(), AnkoLogger {
    private val apiService = RequestClient.buildService(GitHubService::class.java)
    private var prefBasicAuth : String by Preference(this, PrefProperty.AUTH_BASIC, "")
    private var prefTokenAuth : String by Preference(this, PrefProperty.AUTH_TOKEN, "")
    private var prefLoginSuccessful : Boolean by Preference(this, PrefProperty.LOGIN_SUCCESSFUL, false)
/*
    private val mUI: LoginActivityUI by lazy {
        LoginActivityUI()
    }
*/

    private val mBinding by lazy {
        DataBindingUtil.setContentView<LoginActivityBinding>(this, R.layout.login_activity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.activity = this
/*
        mBinding.etLoginPassword.setImeActionLabel(resources.getString(R.string.hub_action_sign_in_short), EditorInfo.IME_ACTION_DONE)
        mBinding.etLoginPassword.imeOptions = EditorInfo.IME_NULL
        mBinding.etLoginPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
*/

        mBinding.etLoginPassword.setOnEditorActionListener { view: TextView, actionId, _ ->
            if (actionId == R.integer.login_action_id || actionId == EditorInfo.IME_NULL) {
                onClickLogin(view)
                return@setOnEditorActionListener true
            } else{
                return@setOnEditorActionListener false
            }
        }

        mBinding.etLoginPassword.textChangedListener {
            afterTextChanged {
                mBinding.etLoginPasswordLayout.isPasswordVisibilityToggleEnabled = mBinding.etLoginPassword.text.toString().isNotEmpty()
            }
        }
/*
        mUI.setContentView(this)
        //必须在AnkoComponent之外设置inputType，否则不生效
        mUI.etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
*/
    }

    fun onClickLogin(view: View) {
        if(mBinding.etLoginUsername.text.isNullOrBlank()) {
            mBinding.etLoginUsername.error = getString(R.string.hub_error_field_required)
            mBinding.etLoginUsername.requestFocus()
            return
        }
        if(mBinding.etLoginPassword.text.isNullOrBlank()) {
            mBinding.etLoginPassword.error = getString(R.string.hub_error_field_required)
            mBinding.etLoginPassword.requestFocus()
            return
        }
        prefTokenAuth = ""
        prefLoginSuccessful = false
        prefBasicAuth = Credentials.basic(mBinding.etLoginUsername.text.toString(), mBinding.etLoginPassword.text.toString())
        apiService.postAuthorizations().flatMap {
            prefTokenAuth = it.token
            apiService.getCurrentUser()
        }.schedule().subscribeBy(
                onNext = {
                    var prefUsername : String by Preference(this, PrefProperty.USERNAME,  "")
                    prefUsername = it.login
                    var prefAvatar : String by Preference(this, PrefProperty.USER_AVATAR,  "")
                    prefAvatar = it.avatar_url
                    prefLoginSuccessful = true
                    toast(String.format(getString(R.string.hub_toast_login_successful), prefUsername))
                    finish()
                },onError = {
            mBinding.etLoginUsername.requestFocus()
            if(it is HttpException) {
                toast("${it.getErrResponse()?.body?.message}")
            }
        }
        )
    }

/*
    private fun attemptLogin() {
        if(mUI.etUsername.text.isNullOrBlank()) {
            mUI.etUsername.error = getString(R.string.hub_error_field_required)
            mUI.etUsername.requestFocus()
            return
        }
        if(mUI.etPassword.text.isNullOrBlank()) {
//            mUI.layoutPassword.isPasswordVisibilityToggleEnabled = false
            mUI.etPassword.error = getString(R.string.hub_error_field_required)
            mUI.etPassword.requestFocus()
            return
        }
        prefTokenAuth = ""
        prefLoginSuccessful = false
        prefBasicAuth = Credentials.basic(mUI.etUsername.text.toString(), mUI.etPassword.text.toString())
        apiService.postAuthorizations().flatMap {
            prefTokenAuth = it.token
            apiService.getCurrentUser()
        }.schedule().subscribeBy(
                onNext = {
                    var prefUsername : String by Preference(this, PrefProperty.USERNAME,  "")
                    prefUsername = it.login
                    var prefAvatar : String by Preference(this, PrefProperty.USER_AVATAR,  "")
                    prefAvatar = it.avatar_url
                    prefLoginSuccessful = true
                    toast("登录成功 sign by $prefUsername")
                    finish()
                },onError = {
                    mUI.etUsername.requestFocus()
                    if(it is HttpException) {
                        toast("${it.getErrResponse()?.body?.message}")
                    }
                }
        )
    }
*/

/*
class LoginActivityUI: AnkoComponent<LoginActivity> {
    lateinit var etUsername: EditText
    lateinit var etPassword: TextInputEditText
    lateinit var layoutPassword: TextInputLayout
    lateinit var btnSignIn: Button
    private val customStyle = { view : Any ->
        when(view) {
            is EditText -> with(view) {
                width = matchParent
                height = wrapContent
                maxLines = 1
                singleLine = true
            }
        }
    }
    override fun createView(ui: AnkoContext<LoginActivity>) = ui.apply{
        frameLayout {
            lparams(matchParent, matchParent)
            linearLayout {
                orientation = LinearLayout.VERTICAL
                textInputLayout {
                    etUsername = editText {
                        hintResource = R.string.hub_prompt_username
                        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                        setText("conghaonet@gmail.com")
                    }
                }.lparams(matchParent, wrapContent)
                layoutPassword = textInputLayout {
                    etPassword = textInputEditText {
                        hintResource = R.string.hub_prompt_password
                        setImeActionLabel(resources.getString(R.string.hub_action_sign_in_short), EditorInfo.IME_ACTION_DONE)
                        imeOptions = EditorInfo.IME_NULL
                        textChangedListener {
                            afterTextChanged {
                                this@textInputLayout.isPasswordVisibilityToggleEnabled = this@textInputEditText.text.toString().isNotEmpty()
                            }
                        }
                        setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
                            if (actionId == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                                owner.attemptLogin()
                                return@OnEditorActionListener true
                            }
                            false
                        })
                    }
                }.lparams(matchParent, wrapContent)
                btnSignIn = button (R.string.hub_action_sign_in){
                    onClick {
                        owner.attemptLogin()
                    }
                }.lparams(matchParent, wrapContent)
            }.lparams(matchParent, matchParent)
        }.applyRecursively(customStyle)
    }.view
}
*/

}

