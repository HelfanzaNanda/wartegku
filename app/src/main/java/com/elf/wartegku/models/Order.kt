package com.elf.wartegku.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("store_id") var store_id : Int? = null,
    @SerializedName("foods") var foods : List<FoodSelected> = mutableListOf()
) : Parcelable
