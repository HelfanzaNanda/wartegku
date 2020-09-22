package com.elf.wartegku.ui.checkout

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elf.wartegku.models.Food
import com.elf.wartegku.models.FoodSelected
import com.elf.wartegku.models.Order
import com.elf.wartegku.repositories.OrderRepository
import com.elf.wartegku.utils.SingleLiveEvent
import com.elf.wartegku.utils.SingleResponse

class CheckoutViewModel (private val orderRepository: OrderRepository) : ViewModel(){
    private val state : SingleLiveEvent<CheckoutState> = SingleLiveEvent()
    private val selectedFoods = MutableLiveData<List<FoodSelected>>()

    private fun isLoading(b :Boolean){ state.value = CheckoutState.Loading(b) }
    private fun toast(m : String){ state.value = CheckoutState.ShowToast(m) }
    private fun success() { state.value = CheckoutState.Success }

    fun setSelectedFoods(foods: List<Food>) {
        val temp = if (selectedFoods.value == null){
            mutableListOf()
        }else{
            selectedFoods.value as MutableList<FoodSelected>
        }
        val x = temp.filterNot { it.id != null }
        x.let {
            foods.forEach {food ->
                temp.add(food.toSelectedFood())
            }
        }
        selectedFoods.postValue(temp)
    }

    private fun Food.toSelectedFood() = FoodSelected(id = id, price = price, qty = qty)

    fun order(token : String, order: Order){
        isLoading(true)
        orderRepository.order(token, order, object : SingleResponse<Order>{
            override fun onSuccess(data: Order?) {
                isLoading(false)
                data?.let { success() }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun listenToState() = state
    fun listenToSelectedFoods() = selectedFoods

}

sealed class CheckoutState{
    data class Loading(var state : Boolean = false) : CheckoutState()
    data class ShowToast(var message : String) : CheckoutState()
    object Success : CheckoutState()
}