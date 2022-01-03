package com.choptius.spec.domain

interface AstroRepository {

    fun getStars(catalog: CharSequence, number: CharSequence): List<Star>

    fun getStarsByFlamsteed(flamsteed: Int, constellation: AstronomicalObject.Constellation): List<Star>

    fun getDeepSkyObjects(catalog: CharSequence, number: CharSequence): List<DeepSkyObject>

    fun setIsInFavorites(astronomicalObject: AstronomicalObject, isInFavorites: Boolean)

    fun getFavorites(): List<AstronomicalObject>

}