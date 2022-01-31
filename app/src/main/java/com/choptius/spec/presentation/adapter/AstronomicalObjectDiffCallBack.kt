package com.choptius.spec.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.choptius.spec.domain.model.AstronomicalObject

class AstronomicalObjectDiffCallBack : DiffUtil.ItemCallback<AstronomicalObject>() {

    override fun areItemsTheSame(
        oldItem: AstronomicalObject,
        newItem: AstronomicalObject
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: AstronomicalObject,
        newItem: AstronomicalObject
    ): Boolean {
        return oldItem.isInFavorites == newItem.isInFavorites
    }
}