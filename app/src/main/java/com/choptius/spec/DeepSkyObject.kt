package com.choptius.spec

import com.choptius.spec.astro.AstronomicalObject

data class DeepSkyObject(
    override val declination: Double,
    override val rightAscension: Double,
    override val constellation: Constellation,
    override val magnitude: Double,
    override val commonName: CharSequence,
    val primaryCatalogName: CharSequence,
    val primaryNumberID: Int,
    val type: NGCType,
    override var isInFavorites: Boolean

) : AstronomicalObject(
    declination,
    rightAscension,
    constellation,
    magnitude,
    commonName,
    isInFavorites

) {

    enum class NGCType(name: CharSequence, imageResource: Int) {
        Ast("Asterism", R.drawable.stars),
        Gxy("Galaxy", R.drawable.ic_galaxy),
        GxyCld("Bright cloud/knot in a galaxy", R.drawable.horse_nebula),
        GC("Globular Cluster", R.drawable.stars),
        HIIRgn("HII Region", R.drawable.horse_nebula),
        Neb("Nebula", R.drawable.horse_nebula),
        NF("Not Found", R.drawable.ic_galaxy),
        OC("Open Cluster", R.drawable.stars),
        PN("Planetary Nebula", R.drawable.horse_nebula),
        SNR("Supernova Remnant", R.drawable.ic_galaxy),
        MWSC("Milky Way Star Cloud", R.drawable.horse_nebula),
        SS("Single Star", R.drawable.stars),
        DS("Double Star", R.drawable.stars),
        TS("Triple Star", R.drawable.stars),
        OSPlusNeb("Open Cluster with Nebula", R.drawable.horse_nebula);

        val fullName: String
        val imageResource: Int

        init {
            fullName = name.toString()
            this.imageResource = imageResource
        }
    }

}
