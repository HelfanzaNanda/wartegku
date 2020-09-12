package com.elf.wartegku.ui.result_search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elf.wartegku.models.Store
import com.elf.wartegku.repositories.StoreRepository
import com.elf.wartegku.utils.ArrayResponse
import com.elf.wartegku.utils.SingleLiveEvent

class ResultSearchViewModel (private val storeRepository: StoreRepository) : ViewModel(){
    private val state : SingleLiveEvent<ResultSearchState> = SingleLiveEvent()
    private val stores = MutableLiveData<List<Store>>()

    private fun isLoading(b : Boolean){ state.value = ResultSearchState.Loading(b) }
    private fun toast(m : String){ state.value = ResultSearchState.ShowToast(m) }

    fun searchStoresByFood(key : String){
        isLoading(true)
//        storeRepository.searchStoresByFood(key, object : ArrayResponse<Store>{
//            override fun onSuccess(datas: List<Store>?) {
//                isLoading(false)
//                datas?.let { stores.postValue(it) }
//            }
//
//            override fun onFailure(err: Error?) {
//                isLoading(false)
//                err?.let { toast(it.message.toString()) }
//            }
//        })
    }

    fun listenToState() = state
    fun listenToStores() = stores
}

sealed class ResultSearchState {
    data class Loading(var state: Boolean) : ResultSearchState()
    data class ShowToast(var message: String) : ResultSearchState()
}