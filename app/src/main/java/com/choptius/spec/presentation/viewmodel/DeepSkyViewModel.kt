package com.choptius.spec.presentation.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.choptius.spec.db.AstroDatabase
import com.choptius.spec.domain.entities.AstronomicalObject
import com.choptius.spec.domain.usecases.FavoritesUseCase
import com.choptius.spec.domain.usecases.GetDeepSkyObjectsUseCase

class DeepSkyViewModel(application: Application) : AndroidViewModel(application) {

    val deepSkyObjectsList = MutableLiveData<List<AstronomicalObject>>()
    private val favoritesUseCase = FavoritesUseCase(AstroDatabase.getInstance(application))
    private val getDeepSkyObjectsUseCase = GetDeepSkyObjectsUseCase(AstroDatabase.getInstance(application))

    fun addToFavorites(obj: AstronomicalObject) {
        Thread{
            favoritesUseCase.addToFavorites(obj)
        }.start()
    }

    fun deleteFromFavorites(obj: AstronomicalObject) {
        Thread {
            favoritesUseCase.deleteFromFavorites(obj)
        }.start()
    }

    fun getDeepSkyObjects(catalog: CharSequence, number: CharSequence) {
        Thread {
            deepSkyObjectsList.postValue(getDeepSkyObjectsUseCase.getDeepSkyObjects(catalog, number))
        }.start()
    }




}