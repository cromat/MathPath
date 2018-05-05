package com.example.cromat.mathpath.model
import com.example.cromat.mathpath.R

class Pet {
    val petItems: List<PetItem> = listOf(
            PetItem(name = "Drink", price = 5, permanent = false, activated = false, bought = false, picture = R.drawable.item_drink),
            PetItem(name = "Food", price = 10, permanent = false, activated = false, bought = false, picture = R.drawable.item_food),
            PetItem(name = "Toy", price = 25, permanent = true, activated = false, bought = false, picture = R.drawable.item_ball)
    )
}
