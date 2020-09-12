package com.elf.wartegku.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.elf.wartegku.R
import com.elf.wartegku.ui.main.MainActivity
import com.elf.wartegku.utils.Constants
import com.elf.wartegku.utils.ext.gone
import com.elf.wartegku.utils.ext.toast
import com.elf.wartegku.utils.ext.visible
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginActivity : AppCompatActivity() {

    private val loginViewModel : LoginViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        observe()
        doLogin()
    }

    private fun observe() = loginViewModel.listenToState().observer(this, Observer { handleUIState(it) })

    private fun handleUIState(loginState: LoginState?) {
        loginState?.let {
            when(it){
                is LoginState.Loading -> handleLoading(it.state)
                is LoginState.ShowToast -> toast(it.message)
                is LoginState.Success -> handleSuccess(it.token)
                is LoginState.Reset -> handleReset()
                is LoginState.Validate -> handleValidate(it)
            }
        }
    }

    private fun handleSuccess(token: String) {
        Constants.setToken(this@LoginActivity, "Bearer $token")
        startActivity(Intent(this@LoginActivity, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }).also { finish() }
    }

    private fun handleLoading(b: Boolean) {
        btn_login.isEnabled = !b
        if (b){
            val doubleBounce = DoubleBounce() as Sprite
            loading.setIndeterminateDrawable(doubleBounce)
            loading.visible()
        }else{
            loading.gone()
        }
    }

    private fun handleValidate(validate: LoginState.Validate) {
        validate.email?.let { setEmailErr(it) }
        validate.password?.let { setPasswordErr(it) }
    }

    private fun handleReset() {
        setEmailErr(null)
        setPasswordErr(null)
    }

    private fun doLogin() {
        btn_login.setOnClickListener {
            val email = et_email.text.toString().trim()
            val pass = et_pass.text.toString().trim()
            if (loginViewModel.validate(email, pass)){
                loginViewModel.login(email, pass)
            }
        }
    }

    private fun setEmailErr(err : String?) { til_email.error = err }
    private fun setPasswordErr(err : String?) { til_pass.error = err }

    override fun onResume() {
        super.onResume()
        if (Constants.getToken(this@LoginActivity) != "UNDEFINED"){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }
}