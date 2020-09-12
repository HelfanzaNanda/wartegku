package com.elf.wartegku.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.elf.wartegku.models.User
import com.elf.wartegku.repositories.UserRepository
import com.elf.wartegku.utils.Constants
import com.elf.wartegku.utils.SingleLiveEvent
import com.elf.wartegku.utils.SingleResponse

class LoginViewModel (private val userRepository: UserRepository) : ViewModel(){
    private val state : SingleLiveEvent<LoginState> = SingleLiveEvent()
    private fun isLoading(b : Boolean) { state.value =  LoginState.Loading(b)}
    private fun toast(m : String) { state.value = LoginState.ShowToast(m) }
    private fun success(t : String){ state.value = LoginState.Success(t) }

    fun validate(email: String, password: String) : Boolean {
        state.value = LoginState.Reset
        if (email.isEmpty()){
            state.value = LoginState.Validate(email = "email tidak boleh kosong")
            return false
        }
        if (!Constants.isValidEmail(email)){
            state.value = LoginState.Validate(email = "email harus mengandung @ dan .")
            return false
        }

        if (password.isEmpty()){
            state.value = LoginState.Validate(password = "password tidak boleh kosong")
            return false
        }

        if (!Constants.isValidPassword(password)){
            state.value = LoginState.Validate(password = "password minimal 8 karakter")
            return false
        }

        return  true
    }

    fun login(email : String, password : String){
        isLoading(true)
        userRepository.login(email, password, object : SingleResponse<User>{
            override fun onSuccess(data: User?) {
                isLoading(false)
                data?.let { success(it.token!!) }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun listenToState() = state
}

sealed class LoginState{
    data class Loading(var state : Boolean = false) : LoginState()
    data class ShowToast(var message : String) : LoginState()
    data class Success(var token : String) : LoginState()
    object Reset : LoginState()
    data class Validate(
        var email: String? = null,
        var password: String? = null
    ) : LoginState()
}