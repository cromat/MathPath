package com.example.cromat.mathpath.model

class Pet {
    val petItems: List<PetItem> = listOf(
            PetItem(name = "Drink", price = 5, permanent = false, activated = false, bought = false, picture = ""),
            PetItem(name = "Food", price = 10, permanent = false, activated = false, bought = false, picture = ""),
            PetItem(name = "Toy", price = 25, permanent = true, activated = false, bought = false, picture = "")
    )
}
