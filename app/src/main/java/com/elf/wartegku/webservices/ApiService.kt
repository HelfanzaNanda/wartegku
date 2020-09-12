package com.elf.wartegku.webservices

import com.elf.wartegku.models.Category
import com.elf.wartegku.models.Food
import com.elf.wartegku.models.Store
import com.elf.wartegku.models.User
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("api/login")
    fun login(
        @Field("email") email : String,
        @Field("password") password : String
    ) : Call<WrappedResponse<User>>

    @FormUrlEncoded
    @POST("api/register")
    fun register(
        @Field("name") name : String,
        @Field("email") email : String,
        @Field("password") password : String
    ) : Call<WrappedResponse<User>>

    @GET("api/profile")
    fun profile(
        @Header("Authorization") token : String
    ) : Call<WrappedResponse<User>>

    @GET("api/category")
    fun fetchCategories() : Call<WrappedListResponse<Category>>

    @GET("api/category/food")
    fun fetchCategoriesByFood() : Call<WrappedListResponse<Category>>

    @GET("api/category/drink")
    fun fetchCategoriesByDrink() : Call<WrappedListResponse<Category>>

    @GET("api/store")
    fun fetchStoresByCategory(
        @Path("category_id") category_id : Int
    ) : Call<WrappedListResponse<Store>>

    @GET("api/store/{key}")
    fun searchStoresByFood(
        @Path("key") key : String
    ) : Call<WrappedListResponse<Store>>

    @GET("api/food/{store_id}")
    fun fetchFoodsByStore(
        @Path("store_id") store_id : Int
    ) : Call<WrappedListResponse<Food>>
}

data class WrappedResponse<T>(
    @SerializedName("message") var message : String?,
    @SerializedName("status") var status : Boolean?,
    @SerializedName("data") var data : T?
)

data class WrappedListResponse<T>(
    @SerializedName("message") var message : String?,
    @SerializedName("status") var status : Boolean?,
    @SerializedName("data") var data : List<T>?
)