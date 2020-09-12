package com.elf.wartegku.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.elf.wartegku.R
import com.elf.wartegku.ui.login.LoginActivity
import com.elf.wartegku.utils.ext.gone
import com.elf.wartegku.utils.ext.toast
import com.elf.wartegku.utils.ext.visible
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce
import kotlinx.android.synthetic.main.activity_register.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private val registerViewModel : RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        observe()
        register()
        label_login.setOnClickListener { goToLogin() }
    }

    private fun observe() = registerViewModel.listenToState().observer(this, Observer { handleUiState(it) })
    private fun goToLogin() = startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))

    private fun handleUiState(registerState: RegisterState?) {
        registerState?.let {
            when(it){
                is RegisterState.Loading -> handleLoading(it.state)
                is RegisterState.ShowToast -> toast(it.message)
                is RegisterState.Reset -> handleReset()
                is RegisterState.Validate -> handleValidate(it)
                is RegisterState.Success -> handleSuccess()
            }
        }
    }

    private fun handleLoading(b: Boolean) {
        btn_reg.isEnabled = !b
        if (b){
            val doubleBounce = DoubleBounce() as Sprite
            loading.setIndeterminateDrawable(doubleBounce)
            loading.visible()
        }else{
            loading.gone()
        }
    }

    private fun handleValidate(validate: RegisterState.Validate) {
        validate.name?.let { setErrorName(it) }
        validate.email?.let { setErrorEmail(it) }
        validate.password?.let { setErrorPass(it) }
    }

    private fun handleReset() {
        setErrorName(null)
        setErrorEmail(null)
        setErrorPass(null)
    }

    private fun handleSuccess() = alert("berhasil registere, silahkan login")
    private fun setErrorName(err : String?){ til_name.error = err }
    private fun setErrorEmail(err : String?){ til_email.error = err }
    private fun setErrorPass(err : String?){ til_pass.error = err }

    private fun alert(m : String){
        AlertDialog.Builder(this).apply {
            setMessage(m)
            setPositiveButton("ya"){dialogInterface, i ->
                goToLogin()
                finish()
                dialogInterface.dismiss()
            }
        }.show()
    }

    private fun register() {
        btn_reg.setOnClickListener {
            val name = et_name.text.toString().trim()
            val email = et_email.text.toString().trim()
            val pass = et_pass.text.toString().trim()
            if (registerViewModel.validate(name, email, pass)){
                registerViewModel.register(name, email, pass)
            }
        }
    }
}