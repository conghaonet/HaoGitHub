package com.app2m.github.hub

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.text.InputType
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.app2m.github.hub.base.BaseActivity
import com.app2m.github.network.*
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.Credentials
import org.jetbrains.anko.*
import org.jetbrains.anko.design.textInputEditText
import org.jetbrains.anko.design.textInputLayout
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.textChangedListener
import retrofit2.HttpException

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : BaseActivity(), AnkoLogger {
    private val apiService = RequestClient.buildService(GitHubService::class.java)
    private var prefBasicAuth : String by Preference(this, PrefProperty.AUTH_BASIC, "")
    private var prefTokenAuth : String by Preference(this, PrefProperty.AUTH_TOKEN, "")
    private var prefLoginSuccessful : Boolean by Preference(this, PrefProperty.LOGIN_SUCCESSFUL, false)
    private val mUI: LoginActivityUI by lazy {
        LoginActivityUI()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUI.setContentView(this)
        //必须在AnkoComponent之外设置inputType，否则不生效
        mUI.etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
/*
        setContentView(R.layout.activity_login)
        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        email_sign_in_button.setOnClickListener { attemptLogin() }
*/
    }

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
            apiService.getUser()
        }.schedule().subscribeBy(
                onNext = {
                    var prefUsername : String by Preference(this, PrefProperty.USERNAME,  "")
                    prefUsername = it.login
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
            verticalLayout {
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

}

