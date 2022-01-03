package com.choptius.spec.db

import android.content.Context
import android.database.Cursor
import android.util.Log
import com.choptius.spec.data.AstroDatabaseHelper
import com.choptius.spec.domain.AstroRepository
import com.choptius.spec.domain.Star
import com.choptius.spec.domain.AstronomicalObject
import com.choptius.spec.domain.DeepSkyObject
import java.lang.Exception
import java.util.ArrayList

class AstroDatabase private constructor(context: Context): AstroRepository {

    private val helper: AstroDatabaseHelper =
        AstroDatabaseHelper(context)


    companion object {

        private var instance: AstroDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AstroDatabase {
            instance?.let {
                return it
            }
            val resultInstance = AstroDatabase(context)
            instance = resultInstance
            return resultInstance

        }

        const val TAG = "AstroDatabase"

        const val MESSAGE = "Failed: "
        const val DEEP_SKY_TABLE = "dso"
        const val STARS_TABLE = "hygdata"
        const val RIGHT_ASCENSION_COLUMN = "RightAscension"
        const val DECLINATION_COLUMN = "Declination"
        const val NGC_TYPE_COLUMN = "NGCType"
        const val CONSTELLATION_COLUMN = "Constellation"
        const val MAGNITUDE_COLUMN = "Magnitude"
        const val COMMON_NAME_COLUMN = "CommonName"
        const val PRIMARY_NUMBER_ID_COLUMN = "PrimaryNumberID"
        const val PRIMARY_CATALOG_NAME_COLUMN = "PrimaryCatalogName"
        const val HIPPARCOS_ID_COLUMN = "HipparcosID"
        const val HENRY_DRAPER_ID_COLUMN = "HenryDraperID"
        const val HARVARD_REVISED_ID_COLUMN = "HarvardRevisedID"
        const val BAYER_COLUMN = "Bayer"
        const val FLAMSTEED_COLUMN = "Flamsteed"
        const val IS_IN_FAVORITES_COLUMN = "isInFavorites"

        private val starCatalogs: Map<String, String> = mapOf(
            "HIP" to HIPPARCOS_ID_COLUMN,
            "HD" to HENRY_DRAPER_ID_COLUMN,
            "HR" to HARVARD_REVISED_ID_COLUMN
        )

    }


    override fun getFavorites(): List<AstronomicalObject> {

            val favorites: MutableList<AstronomicalObject> = ArrayList()

            val query = "SELECT * FROM $DEEP_SKY_TABLE WHERE $IS_IN_FAVORITES_COLUMN = 1"
            val query1 = "SELECT * FROM $STARS_TABLE WHERE $IS_IN_FAVORITES_COLUMN = 1"

            helper.readableDatabase.use { database ->
                database.rawQuery(query, null).use { deepSkyCursor ->
                    database.rawQuery(query1, null).use { starsCursor ->
                        addAllDSO(favorites, deepSkyCursor)
                        addAllStars(favorites, starsCursor)
                    }
                }
            }
            return favorites
        }

    override fun getStars(catalog: CharSequence, number: CharSequence): List<Star> {

        val catalogNameidColumn = starCatalogs[catalog.toString()]
        val stars: MutableList<Star> = ArrayList()

        val query = "SELECT * FROM $STARS_TABLE WHERE " +
                "CAST($catalogNameidColumn as TEXT) like '$number%' GROUP BY $PRIMARY_NUMBER_ID_COLUMN"

        val cursor = helper.readableDatabase.rawQuery(query, null)

        try {
            cursor.use { addAllStars(stars, cursor) }

        } catch (e: Exception) {
            Log.e(TAG, MESSAGE, e)
            return stars
        }
        return stars
    }

    override fun getStarsByFlamsteed(flamsteed: Int, constellation: AstronomicalObject.Constellation): List<Star> {

        val stars: MutableList<Star> = ArrayList()

        val query = "SELECT * FROM $STARS_TABLE WHERE " +
                "CAST($FLAMSTEED_COLUMN as TEXT) like '$flamsteed%'  AND " +
                "WHERE $CONSTELLATION_COLUMN = ${constellation.name} " +
                "GROUP BY $FLAMSTEED_COLUMN"

        val cursor = helper.readableDatabase.rawQuery(query, null)

        try {
            cursor.use { addAllStars(stars, cursor) }

        } catch (e: Exception) {
            Log.e(TAG, MESSAGE, e)
            return stars
        }
        return stars
    }

    override fun getDeepSkyObjects(catalog: CharSequence, number: CharSequence): List<DeepSkyObject> {

        val list: MutableList<DeepSkyObject> = ArrayList()

        val query =
            "SELECT * FROM $DEEP_SKY_TABLE WHERE $PRIMARY_CATALOG_NAME_COLUMN = '$catalog' AND " +
                    "CAST($PRIMARY_NUMBER_ID_COLUMN as TEXT) like '$number%' GROUP BY $PRIMARY_NUMBER_ID_COLUMN"

        val cursor = helper.readableDatabase.rawQuery(query, null)

        try {
            cursor.use { addAllDSO(list, it) }

        } catch (e: Exception) {
            Log.e(TAG, MESSAGE, e)
            return emptyList()

        }

        return list
    }

    override fun setIsInFavorites(astronomicalObject: AstronomicalObject, addOrDelete: Boolean) {

        val toAddOrToDelete = if (addOrDelete) 1 else 0

        try {
            helper.writableDatabase.use {

                if (astronomicalObject is Star) {
                    it.execSQL(
                        "UPDATE $STARS_TABLE SET $IS_IN_FAVORITES_COLUMN = $toAddOrToDelete WHERE $HIPPARCOS_ID_COLUMN = ${astronomicalObject.hIPid}"
                    )

                } else if (astronomicalObject is DeepSkyObject) {

                    val query =
                        "UPDATE $DEEP_SKY_TABLE SET $IS_IN_FAVORITES_COLUMN = $toAddOrToDelete " +
                                "WHERE $PRIMARY_CATALOG_NAME_COLUMN = '${astronomicalObject.primaryCatalogName}' " +
                                "AND $PRIMARY_NUMBER_ID_COLUMN = ${astronomicalObject.primaryNumberID}"

                    it.execSQL(query)

                    Log.e(TAG, """ Changed: ${astronomicalObject.primaryCatalogName}${astronomicalObject.primaryNumberID}  $query  """.trimIndent())
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, "setIsInFavorites operation failed", e)
        }
    }

    private fun addAllStars(list: MutableList<in Star>, cursor: Cursor) {

        cursor.use {
            while (it.moveToNext()) {
                list.add(
                    Star(
                        it.getDouble(it.getColumnIndexOrThrow(DECLINATION_COLUMN)),
                        it.getDouble(it.getColumnIndexOrThrow(RIGHT_ASCENSION_COLUMN)),
                        AstronomicalObject.Constellation.fromString(
                            it.getString(
                                it.getColumnIndexOrThrow(
                                    CONSTELLATION_COLUMN
                                )
                            )
                        ),
                        it.getDouble(it.getColumnIndexOrThrow(MAGNITUDE_COLUMN)),
                        it.getString(it.getColumnIndexOrThrow(COMMON_NAME_COLUMN)),
                        it.getInt(it.getColumnIndexOrThrow(HIPPARCOS_ID_COLUMN)),
                        it.getInt(it.getColumnIndexOrThrow(HENRY_DRAPER_ID_COLUMN)),
                        it.getInt(it.getColumnIndexOrThrow(HARVARD_REVISED_ID_COLUMN)),
                        it.getString(it.getColumnIndexOrThrow(BAYER_COLUMN)),
                        it.getInt(it.getColumnIndexOrThrow(FLAMSTEED_COLUMN)),
                        it.getInt(it.getColumnIndexOrThrow(IS_IN_FAVORITES_COLUMN)) == 1
                    )
                )
            }
        }
    }

    //аналогично методу addStars
    private fun addAllDSO(list: MutableList<in DeepSkyObject>, cursor: Cursor) {

        cursor.use {

            while (it.moveToNext()) {

                list.add(
                    DeepSkyObject(
                        it.getDouble(it.getColumnIndexOrThrow(DECLINATION_COLUMN)),
                        it.getDouble(it.getColumnIndexOrThrow(RIGHT_ASCENSION_COLUMN)),
                        AstronomicalObject.Constellation.valueOf(
                            it.getString(
                                it.getColumnIndexOrThrow(
                                    CONSTELLATION_COLUMN
                                )
                            )
                        ),
                        it.getDouble(it.getColumnIndexOrThrow(MAGNITUDE_COLUMN)),
                        it.getString(it.getColumnIndexOrThrow(COMMON_NAME_COLUMN)),
                        it.getString(it.getColumnIndexOrThrow(PRIMARY_CATALOG_NAME_COLUMN)),
                        it.getInt(it.getColumnIndexOrThrow(PRIMARY_NUMBER_ID_COLUMN)),
                        DeepSkyObject.NGCType.valueOf(
                            it.getString(
                                it.getColumnIndexOrThrow(
                                    NGC_TYPE_COLUMN
                                )
                            )
                        ),
                        it.getInt(it.getColumnIndexOrThrow(IS_IN_FAVORITES_COLUMN)) == 1
                    )
                )
            }
        }
    }

}