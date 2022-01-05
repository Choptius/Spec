package com.choptius.spec.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.choptius.spec.db.AstroDatabase
import com.choptius.spec.domain.entities.AstronomicalObject
import com.choptius.spec.domain.usecases.FavoritesUseCase
import com.choptius.spec.domain.usecases.GetDeepSkyObjectsUseCase

class DeepSkyViewModel(context: Context) : ViewModel() {

    val deepSkyObjectsList = MutableLiveData<List<AstronomicalObject>>()
    private val favoritesUseCase = FavoritesUseCase(AstroDatabase.getInstance(context))
    private val getDeepSkyObjectsUseCase = GetDeepSkyObjectsUseCase(AstroDatabase.getInstance(context))

    fun addToFavorites(obj: AstronomicalObject) = favoritesUseCase.addToFavorites(obj)

    fun deleteFromFavorites(obj: AstronomicalObject) = favoritesUseCase.deleteFromFavorites(obj)

    fun getDeepSkyObjects(catalog: CharSequence, number: CharSequence) {
        deepSkyObjectsList.value = getDeepSkyObjectsUseCase.getDeepSkyObjects(catalog, number)
    }




}