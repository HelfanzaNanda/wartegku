package com.elf.wartegku.repositories

import com.elf.wartegku.models.Category
import com.elf.wartegku.utils.ArrayResponse
import com.elf.wartegku.webservices.ApiService
import com.elf.wartegku.webservices.WrappedListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface CategoryContract{
    fun fetchCategoriesByFood(listener : ArrayResponse<Category>)
    fun fetchCategoriesByDrink(listener : ArrayResponse<Category>)
}

class CategoryRepository (private val api : ApiService) : CategoryContract {

    override fun fetchCategoriesByFood(listener: ArrayResponse<Category>) {
        api.fetchCategoriesByFood().enqueue(object : Callback<WrappedListResponse<Category>>{
            override fun onFailure(call: Call<WrappedListResponse<Category>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Category>>, response: Response<WrappedListResponse<Category>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }

    override fun fetchCategoriesByDrink(listener: ArrayResponse<Category>) {
        api.fetchCategoriesByDrink().enqueue(object : Callback<WrappedListResponse<Category>>{
            override fun onFailure(call: Call<WrappedListResponse<Category>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Category>>, response: Response<WrappedListResponse<Category>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }
}