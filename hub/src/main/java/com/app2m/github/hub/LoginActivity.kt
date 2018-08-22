package com.app2m.github.hub

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.app2m.github.hub.base.BaseActivity
import com.app2m.github.network.*
import io.reactivex.rxkotlin.subscribeBy
import org.jetbrains.anko.*
import org.jetbrains.anko.design.textInputLayout
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : BaseActivity(), AnkoLogger {
    private var prefUsername : String by Preference(this, PrefProperty.USERNAME,  "")
    private var prefPassword : String by Preference(this, PrefProperty.PASSWORD, "")
    private var prefBasicAuth : String by Preference(this, PrefProperty.AUTH_BASIC, "")
    private var prefTokenAuth : String by Preference(this, PrefProperty.AUTH_TOKEN, "")
    private var prefLoginSuccessful : Boolean by Preference(this, PrefProperty.LOGIN_SUCCESSFUL, false)

    private val mUI: LoginActivityUI by lazy {
        LoginActivityUI()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUI.setContentView(this)
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

        if(mUI.etEmail.text.isNullOrBlank()) {
            mUI.etEmail.error = getString(R.string.hub_error_field_required)
            mUI.etEmail.requestFocus()
            return
        }
        if(mUI.etPassword.text.isNullOrBlank()) {
            mUI.etPassword.error = getString(R.string.hub_error_field_required)
            mUI.etPassword.requestFocus()
            return
        }
        prefUsername = mUI.etEmail.text.toString()
        prefPassword = mUI.etPassword.text.toString()
        prefTokenAuth = ""
        prefBasicAuth = getBasicCredentials(prefUsername, prefPassword)
        prefLoginSuccessful = false
        info("username=$prefUsername")
        info("password=$prefPassword")
        info("basic=$prefBasicAuth")
        val apiService = RequestClient.buildService(GitHubService::class.java)
        apiService.postAuthorizations().schedule().subscribeBy (
                onNext = {
                    toast(it.toString())
                    prefTokenAuth = it.token
                    prefLoginSuccessful = true
                },
                onError =  {
                    it.printStackTrace()
                }
        )
    }
/*
    private fun attemptLogin() {
        // Reset errors.
        email.error = null
        password.error = null

        // Store values at the time of the login attempt.
        val emailStr = email.text.toString()
        val passwordStr = password.text.toString()

        var cancel = false
        var focusView: View? = null
        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            email.error = getString(R.string.error_field_required)
            focusView = email
            cancel = true
        } else if (TextUtils.isEmpty(passwordStr)) {
            password.error = getString(R.string.error_field_required)
            focusView = password
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            toast("execute login")
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            */
/*showProgress(true)
            mAuthTask = UserLoginTask(emailStr, passwordStr)
            mAuthTask!!.execute(null as Void?)*//*

        }
    }
*/
class LoginActivityUI: AnkoComponent<LoginActivity> {
    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
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
                    etEmail = editText {
                        hintResource = R.string.hub_prompt_email
                        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                        setText("conghaonet@gmail.com")
                    }
                }.lparams(matchParent, wrapContent)
                textInputLayout {
                    etPassword = editText {
                        hintResource = R.string.hub_prompt_password
                        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                        setImeActionLabel(resources.getString(R.string.hub_action_sign_in_short), EditorInfo.IME_ACTION_DONE)
                        imeOptions = EditorInfo.IME_NULL
                        setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
                            if (actionId == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                                owner.attemptLogin()
                                return@OnEditorActionListener true
                            }
                            false
                        })
                    }
                }.lparams(matchParent, wrapContent)
                btnSignIn = button (R.string.action_sign_in){
                    onClick {
                        owner.attemptLogin()
                    }
                }.lparams(matchParent, wrapContent)
            }.lparams(matchParent, matchParent)
        }.applyRecursively(customStyle)
    }.view
}

}

