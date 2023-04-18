package com.mumford.bookervatcalculator

data class Item(val name: String, val price: Float, val hasVAT: Boolean = true) {
    fun priceWithVAT(): Float {
        return if (hasVAT) price * 1.2f else price
    }
}