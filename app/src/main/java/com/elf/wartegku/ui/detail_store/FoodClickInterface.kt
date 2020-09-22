package com.elf.wartegku.ui.detail_store

import com.elf.wartegku.models.Food

interface FoodClickInterface {
    fun add(food: Food)
    fun increment(food: Food)
    fun decrement(food: Food)
}