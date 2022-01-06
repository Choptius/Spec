package com.choptius.spec.domain

import com.choptius.spec.domain.entities.AstronomicalObject
import com.choptius.spec.domain.entities.DeepSkyObject
import com.choptius.spec.domain.entities.Star

interface AstroRepository {

    fun getStars(catalog: CharSequence, number: CharSequence): List<Star>

    fun getStarsByFlamsteed(flamsteed: Int, constellation: AstronomicalObject.Constellation): List<Star>

    fun getDeepSkyObjects(catalog: CharSequence, number: CharSequence): List<DeepSkyObject>

    fun setIsInFavorites(astronomicalObject: AstronomicalObject, isInFavorites: Boolean)

    fun getFavorites(): List<AstronomicalObject>

}