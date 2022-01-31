package com.choptius.spec.domain.repository

import com.choptius.spec.domain.model.AstronomicalObject
import com.choptius.spec.domain.model.DeepSkyObject
import com.choptius.spec.domain.model.Star

interface AstroRepository {

    fun getStars(catalog: CharSequence, number: CharSequence): List<Star>

    fun getStarsByFlamsteed(flamsteed: Int, constellation: AstronomicalObject.Constellation): List<Star>

    fun getDeepSkyObjects(catalog: CharSequence, number: CharSequence): List<DeepSkyObject>

    fun setIsInFavorites(astronomicalObject: AstronomicalObject, isInFavorites: Boolean)

    fun getFavorites(): List<AstronomicalObject>

}