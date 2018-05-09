package com.example.cromat.mathpath.model

import com.example.cromat.mathpath.R

class Pet {
    val petItems: List<PetItem> = listOf(
            PetItem(name = "Drink", price = 5, permanent = false, activated = false, bought = false, picture = R.drawable.drink),
            PetItem(name = "Food", price = 10, permanent = false, activated = false, bought = false, picture = R.drawable.food),
            PetItem(name = "Ball", price = 25, permanent = true, activated = false, bought = false, picture = R.drawable.ball),
            PetItem(name = "Shirt", price = 50, permanent = true, activated = false, bought = false, picture = R.drawable.shirt)
    )
}
