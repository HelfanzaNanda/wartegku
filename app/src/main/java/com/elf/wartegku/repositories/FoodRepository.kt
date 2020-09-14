package com.elf.wartegku.repositories

import com.elf.wartegku.models.Food
import com.elf.wartegku.utils.ArrayResponse
import com.elf.wartegku.webservices.ApiService
import com.elf.wartegku.webservices.WrappedListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface FoodContract{
    fun fetchFoodsByStore(store_id : String, listener : ArrayResponse<Food>)
    fun fetchFoodsLatest(listener: ArrayResponse<Food>)
}
class FoodRepository (private val api : ApiService) : FoodContract {
    override fun fetchFoodsByStore(store_id: String, listener: ArrayResponse<Food>) {
        api.fetchFoodsByStore(store_id.toInt()).enqueue(object : Callback<WrappedListResponse<Food>>{
            override fun onFailure(call: Call<WrappedListResponse<Food>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Food>>, response: Response<WrappedListResponse<Food>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }

    override fun fetchFoodsLatest(listener: ArrayResponse<Food>) {
        api.fetchFoodsLatest().enqueue(object : Callback<WrappedListResponse<Food>>{
            override fun onFailure(call: Call<WrappedListResponse<Food>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Food>>, response: Response<WrappedListResponse<Food>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }
}