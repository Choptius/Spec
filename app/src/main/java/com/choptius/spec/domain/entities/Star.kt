package com.choptius.spec.domain.entities


data class Star(
    override val declination: Double,
    override val rightAscension: Double,
    override val constellation: Constellation,
    override val magnitude: Double,
    override val commonName: CharSequence,
    val hIPid: Int,
    val hDid: Int,
    val hRid: Int,
    val bayer: CharSequence,
    val flamsteed: Int,
    override var isInFavorites: Boolean

) : AstronomicalObject(
    declination, rightAscension, constellation, magnitude, commonName, isInFavorites
) {
    val cataloguedName = if (hRid != 0) "HR $hRid" else if (hDid != 0) "HD $hDid" else "HIP $hIPid"
}