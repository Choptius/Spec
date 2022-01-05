package com.choptius.spec.domain.usecases

import com.choptius.spec.domain.AstroRepository
import com.choptius.spec.domain.entities.AstronomicalObject

class FavoritesUseCase(private val repository: AstroRepository) {

    fun addToFavorites(astronomicalObject: AstronomicalObject) =
        repository.setIsInFavorites(astronomicalObject, true)

    fun deleteFromFavorites(astronomicalObject: AstronomicalObject) =
        repository.setIsInFavorites(astronomicalObject, false)

}
