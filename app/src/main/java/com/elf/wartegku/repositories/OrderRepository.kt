package com.elf.wartegku.repositories

import com.elf.wartegku.models.Order
import com.elf.wartegku.utils.SingleResponse
import com.elf.wartegku.webservices.ApiService
import com.elf.wartegku.webservices.WrappedResponse
import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface OrderContract{
    fun order(token : String, order: Order, listener : SingleResponse<Order>)
}

class OrderRepository (private val api : ApiService) : OrderContract{
    override fun order(token: String, order: Order, listener: SingleResponse<Order>) {
        val g = GsonBuilder().create()
        val json = g.toJson(order)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        api.order(token, body).enqueue(object : Callback<WrappedResponse<Order>>{
            override fun onFailure(call: Call<WrappedResponse<Order>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Order>>, response: Response<WrappedResponse<Order>>) {
                when{
                    response.isSuccessful -> {
                        val b = response.body()
                        if (b?.status!!) listener.onSuccess(b.data) else listener.onFailure(Error(b.message))
                    }
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

}