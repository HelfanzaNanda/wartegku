package com.elf.wartegku.repositories

import com.elf.wartegku.models.Store
import com.elf.wartegku.utils.ArrayResponse
import com.elf.wartegku.webservices.ApiService
import com.elf.wartegku.webservices.WrappedListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface StoreContract{
    fun searchStoresByFood(key : String, listener : ArrayResponse<Store>)
    fun fetchStoresByCategory(category_id : String, listener: ArrayResponse<Store>)
}

class StoreRepository (private val api : ApiService) : StoreContract {
    override fun searchStoresByFood(key: String, listener: ArrayResponse<Store>) {
        api.searchStoresByFood(key).enqueue(object : Callback<WrappedListResponse<Store>>{
            override fun onFailure(call: Call<WrappedListResponse<Store>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Store>>, response: Response<WrappedListResponse<Store>>) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!){
                            listener.onSuccess(body.data)
                        }else{
                            listener.onFailure(Error(body.message))
                        }
                    }
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun fetchStoresByCategory(category_id: String, listener: ArrayResponse<Store>) {
        api.fetchStoresByCategory(category_id.toInt()).enqueue(object : Callback<WrappedListResponse<Store>>{
            override fun onFailure(call: Call<WrappedListResponse<Store>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Store>>, response: Response<WrappedListResponse<Store>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

}