package com.elf.wartegku.ui.register

import androidx.lifecycle.ViewModel
import com.elf.wartegku.models.User
import com.elf.wartegku.repositories.UserRepository
import com.elf.wartegku.utils.SingleLiveEvent
import com.elf.wartegku.utils.SingleResponse

class RegisterViewModel (private val userRepository: UserRepository) : ViewModel(){
    private val state : SingleLiveEvent<RegisterState> = SingleLiveEvent()
    private fun isLoading(b : Boolean) { state.value =  RegisterState.Loading(b)}
    private fun toast(m : String) { state.value = RegisterState.ShowToast(m) }
    private fun success() { state.value = RegisterState.Success }

    fun register(name : String, email: String, password: String){
        isLoading(true)
        userRepository.register(name, email, password, object : SingleResponse<User>{
            override fun onSuccess(data: User?) {
                data?.let { success() }
            }

            override fun onFailure(err: Error?) {
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
}