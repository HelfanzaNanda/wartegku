package com.elf.wartegku.ui.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elf.wartegku.models.Category
import com.elf.wartegku.repositories.CategoryRepository
import com.elf.wartegku.utils.ArrayResponse
import com.elf.wartegku.utils.SingleLiveEvent

class CategoryViewModel (private val categoryRepository: CategoryRepository) : ViewModel(){
    private val state : SingleLiveEvent<CategoryState> = SingleLiveEvent()
    private val categoriesFood = MutableLiveData<List<Category>>()
    private val categoriesDrink = MutableLiveData<List<Category>>()

    private fun isLoading(b : Boolean){ state.value = CategoryState.Loading(b) }
    private fun toast(m : String){ state.value = CategoryState.ShowToast(m) }

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

    fun listenToCategoriesByFood() = categoriesFood
    fun listenToCategoriesByDrink() = categoriesDrink
    fun listenToState() = state
}

sealed class CategoryState{
    data class Loading(var state : Boolean = false) : CategoryState()
    data class ShowToast(var message : String) : CategoryState()
}