package com.elf.wartegku.ui.food

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elf.wartegku.models.Food
import com.elf.wartegku.repositories.FoodRepository
import com.elf.wartegku.utils.ArrayResponse
import com.elf.wartegku.utils.SingleLiveEvent

class FoodViewModel (private val foodRepository: FoodRepository) : ViewModel(){
    private val state : SingleLiveEvent<FoodState> = SingleLiveEvent()
    private val foods = MutableLiveData<List<Food>>()
    private val selectedFoods = MutableLiveData<List<Food>>()
    private val filteredFoods = MutableLiveData<List<Food>>()
    private val totalItem = MutableLiveData<Int>()
    private val totalPrice = MutableLiveData<Int>()

    private fun isLoading(boolean: Boolean){ state.value = FoodState.Loading(boolean) }
    private fun toast(m: String){ state.value = FoodState.ShowToast(m) }

    fun fetchFoodsByStore(store_id : String) {
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

    fun addSelectedProduct(f: Food){
        val tempSelectedFoods = if(foods.value == null){
            mutableListOf()
        } else {
            foods.value as MutableList<Food>
        }
        val sameProduct = tempSelectedFoods.find { p -> p.id == f.id }
        sameProduct?.let {p ->
            p.qty = p.qty?.plus(1)
        } ?: kotlin.run {
            f.qty = 1
            tempSelectedFoods.add(f)
        }
        foods.postValue(tempSelectedFoods)
    }

    fun decrementQuantity(f: Food) {
        val tempSelectedFoods = if(foods.value == null){
            mutableListOf()
        } else {
            foods.value as MutableList<Food>
        }
        val p = tempSelectedFoods.find { it.id == f.id }
        p?.let {
            if(it.qty?.minus(1) == -1){
                //do something when zero
            }else{
                it.qty = it.qty!!.minus(1)
            }
        }
        foods.postValue(tempSelectedFoods)
    }

    fun incrementQuantity(f: Food){
        val tempSelectedFoods = if(foods.value == null){
            mutableListOf()
        } else {
            foods.value as MutableList<Food>
        }
        val p = tempSelectedFoods.find { it.id == f.id }
        p?.let {
            it.qty = it.qty!!.plus(1)
        }
        foods.postValue(tempSelectedFoods)
    }

    fun deleteSelectedProduct(f: Food){
        val tempSelectedFoods = if(foods.value == null){
            mutableListOf()
        } else {
            foods.value as MutableList<Food>
        }
        val x = tempSelectedFoods.find { it.id == f.id }
        x?.let {
            tempSelectedFoods.remove(it)
        }
        foods.postValue(tempSelectedFoods)
    }

    fun setSelectedFoods(foods: List<Food>) {
        println("foods $foods")
        selectedFoods.postValue(foods)
        println("selected ${selectedFoods.value}")
    }

    fun filterFoods(fs: List<Food>){
        filteredFoods.value = (fs.filter { food ->  food.qty!! > 0 })
    }

    fun setTotalItem(foods: List<Food>) {
        totalItem.value = foods.sumBy { food -> food.qty!! }
    }

    fun setTotalPrice(foods: List<Food>){
        totalPrice.value = foods.sumBy { food -> food.price!!.times(food.qty!!) }
    }

    fun listenToState() = state
    fun listenToFoods() = foods
    fun listenToSelectedFoods() = selectedFoods
    fun listenToFilteredFoods() = filteredFoods
    fun getTotalItem() = totalItem
    fun getTotalPrice() = totalPrice
}

sealed class FoodState{
    data class Loading(var state : Boolean) : FoodState()
    data class ShowToast(var messsage : String) : FoodState()
}