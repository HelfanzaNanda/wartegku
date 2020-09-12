package com.elf.wartegku.ui.detail_store

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elf.wartegku.models.Food
import com.elf.wartegku.repositories.FoodRepository
import com.elf.wartegku.utils.ArrayResponse
import com.elf.wartegku.utils.SingleLiveEvent

class DetailStoreViewModel (private val foodRepository: FoodRepository) : ViewModel(){
    private val state : SingleLiveEvent<DetailStoreState> = SingleLiveEvent()
    private val foods = MutableLiveData<List<Food>>()

    private fun isLoading(boolean: Boolean){ state.value = DetailStoreState.Loading(boolean) }
    private fun toast(m: String){ state.value = DetailStoreState.ShowToast(m) }

    fun fetchFoodsByStore(store_id : String){
        isLoading(true)
        foodRepository.fetchFoodsByStore(store_id, object : ArrayResponse<Food>{
            override fun onSuccess(datas: List<Food>?) {
                isLoading(false)
                datas?.let { foods.postValue(it) }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun listenToState() = state
    fun listenToFoods() = foods

}

sealed class DetailStoreState{
    data class Loading(var state : Boolean) : DetailStoreState()
    data class ShowToast(var messsage : String) : DetailStoreState()
}