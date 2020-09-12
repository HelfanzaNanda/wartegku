package com.elf.wartegku.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("image") var image : String? = null,
    @SerializedName("category") var category : String? = null,
    @SerializedName("is_food") var is_food : Boolean = false
) : Parcelable

@Parcelize
data class FoodByCategory(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("category") var category : String? = null
) : Parcelable

@Parcelize
data class DrinkByCategory(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("category") var category : String? = null
) : Parcelable