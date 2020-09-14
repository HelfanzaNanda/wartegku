package com.elf.wartegku.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Food (
    @SerializedName("id") var id : Int? = null,
    @SerializedName("name") var name : String? = null,
    @SerializedName("description") var description : String? = null,
    @SerializedName("price") var price : Int? = null,
    @SerializedName("image") var image : String? = null,
    var qty : Int? = 0
) : Parcelable


@Parcelize
data class FoodSelected (
    @SerializedName("id") var id : Int? = null,
    @SerializedName("price") var price : Int? = null,
    @SerializedName("qty") var qty : Int? = 0
) : Parcelable