package com.elf.wartegku.ui.register

import androidx.lifecycle.ViewModel
import com.elf.wartegku.models.User
import com.elf.wartegku.repositories.UserRepository
import com.elf.wartegku.utils.Constants
import com.elf.wartegku.utils.SingleLiveEvent
import com.elf.wartegku.utils.SingleResponse

class RegisterViewModel (private val userRepository: UserRepository) : ViewModel(){
    private val state : SingleLiveEvent<RegisterState> = SingleLiveEvent()
    private fun isLoading(b : Boolean) { state.value =  RegisterState.Loading(b)}
    private fun toast(m : String) { state.value = RegisterState.ShowToast(m) }
    private fun success() { state.value = RegisterState.Success }

    fun validate(name: String, email: String, password: String) : Boolean {
        state.value = RegisterState.Reset
        if (name.isEmpty()){
            state.value = RegisterState.Validate(name = "nama tidak boleh kosong")
            return false
        }

        if (email.isEmpty()){
            state.value = RegisterState.Validate(email = "email tidak boleh kosong")
            return false
        }

        if (!Constants.isValidEmail(email)){
            state.value = RegisterState.Validate(email ="format email salah")
            return false
        }

        if (password.isEmpty()){
            state.value = RegisterState.Validate(password = "password tidak boleh kosong")
            return false
        }

        if (!Constants.isValidPassword(password)){
            state.value = RegisterState.Validate(password = "password harus lebih dari 7")
            return false
        }

        return true
    }

    fun register(name : String, email: String, password: String){
        isLoading(true)
        userRepository.register(name, email, password, object : SingleResponse<User>{
            override fun onSuccess(data: User?) {
                isLoading(false)
                data?.let { success() }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(err.message.toString()) }
            }
        })
    }

    fun listenToState() = state
}

sealed class RegisterState{
    data class Loading(var state : Boolean = false) : RegisterState()
    data class ShowToast(var message : String) : RegisterState()
    object Success : RegisterState()
    object Reset : RegisterState()
    data class Validate(
        var name : String? = null,
        var email : String? = null,
        var password : String? = null
    ) : RegisterState()
}