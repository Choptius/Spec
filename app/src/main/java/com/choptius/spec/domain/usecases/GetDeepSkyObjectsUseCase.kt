package com.choptius.spec.domain.usecases

import com.choptius.spec.domain.repository.AstroRepository

class GetDeepSkyObjectsUseCase(private val repository: AstroRepository) {

    fun getDeepSkyObjects(catalog: CharSequence, number: CharSequence) =
        repository.getDeepSkyObjects(catalog, number)

}