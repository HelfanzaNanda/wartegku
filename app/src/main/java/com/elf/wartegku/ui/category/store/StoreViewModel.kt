package com.elf.wartegku.ui.category.store

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elf.wartegku.models.Store
import com.elf.wartegku.repositories.StoreRepository
import com.elf.wartegku.utils.ArrayResponse
import com.elf.wartegku.utils.SingleLiveEvent

class StoreViewModel (private val storeRepository: StoreRepository) : ViewModel(){
    private val state : SingleLiveEvent<StoreState> = SingleLiveEvent()
    private val stores = MutableLiveData<List<Store>>()

    private fun isLoading(b : Boolean){ state.value = StoreState.Loading(b) }
    private fun toast(message: String) { state.value = StoreState.ShowToast(message) }

    fun fetchStoresByCategory(category_id : String){
        isLoading(true)
        storeRepository.fetchStoresByCategory(category_id, object : ArrayResponse<Store>{
            override fun onSuccess(datas: List<Store>?) {
                isLoading(false)
                datas?.let { stores.postValue(it) }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }

        })
    }

    fun listenToState() = state
    fun listenToStores() = stores

}

sealed class StoreState{
    data class Loading(var state : Boolean) : StoreState()
    data class ShowToast(var message : String) : StoreState()
}