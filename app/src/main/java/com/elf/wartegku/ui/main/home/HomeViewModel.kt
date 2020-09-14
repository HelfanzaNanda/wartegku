package com.elf.wartegku.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elf.wartegku.models.Category
import com.elf.wartegku.models.Food
import com.elf.wartegku.repositories.CategoryRepository
import com.elf.wartegku.repositories.FoodRepository
import com.elf.wartegku.utils.ArrayResponse
import com.elf.wartegku.utils.SingleLiveEvent

class HomeViewModel(private val categoryRepository: CategoryRepository,
                    private val foodRepository: FoodRepository) : ViewModel() {

    private val state : SingleLiveEvent<HomeState> = SingleLiveEvent()
    private val categoriesFood = MutableLiveData<List<Category>>()
    private val categoriesDrink = MutableLiveData<List<Category>>()
    private val foods = MutableLiveData<List<Food>>()

    private fun isLoading(b : Boolean){ state.value = HomeState.Loading(b) }
    private fun toast(m : String){ state.value = HomeState.ShowToast(m) }

    fun fetchCategoriesByFood() {
        isLoading(true)
        categoryRepository.fetchCategoriesByFood(object : ArrayResponse<Category> {
            override fun onSuccess(datas: List<Category>?) {
                fetchCategoriesByDrink()
                datas?.let { categoriesFood.postValue(it) }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    private fun fetchCategoriesByDrink() {
        categoryRepository.fetchCategoriesByDrink(object : ArrayResponse<Category> {
            override fun onSuccess(datas: List<Category>?) {
                isLoading(false)
                datas?.let { categoriesDrink.postValue(it) }
            }

            override fun onFailure(err: Error?) {
                isLoading(false)
                err?.let { toast(it.message.toString()) }
            }
        })
    }

    fun fetchFoodsLatest(){
        isLoading(true)
        foodRepository.fetchFoodsLatest(object : ArrayResponse<Food>{
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

    fun listenToCategoriesByFood() = categoriesFood
    fun listenToCategoriesByDrink() = categoriesDrink
    fun listenToFoodsLatest() = foods
    fun listenToState() = state
}

sealed class HomeState{
    data class Loading(var state : Boolean) : HomeState()
    data class ShowToast(var message : String) : HomeState()
}