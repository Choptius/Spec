package com.choptius.spec.presentation.adapter

import android.content.Context
import android.util.Log
import android.util.LogPrinter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.choptius.spec.R
import com.choptius.spec.presentation.adapter.AstroAdapter.AstroHolder
import com.choptius.spec.domain.entities.AstronomicalObject
import com.choptius.spec.domain.entities.DeepSkyObject
import com.choptius.spec.domain.entities.Star

class AstroAdapter : RecyclerView.Adapter<AstroHolder>() {

    var onFavoritesButtonClickListener: ((obj: AstronomicalObject, isChecked: Boolean) -> Unit)? = null
    var count = 0

    var list = listOf<AstronomicalObject>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AstroHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.object_item, parent, false)
        return AstroHolder(view)
    }

    override fun onBindViewHolder(holder: AstroHolder, position: Int) {

        val astronomicalObject = list[position]
        holder.astronomicalObject = astronomicalObject


        if (astronomicalObject.magnitude != 0.0) {
            holder.magnitudeTextView.text = "Magnitude: ${astronomicalObject.magnitude}"
        }
        holder.positionInfoTextView.text = astronomicalObject.constellation.fullName
        holder.addToFavoritesCheckBox.isChecked = astronomicalObject.isInFavorites

        //связываю с полями Star или DeepSkyObject
        if (astronomicalObject is Star) {

            if (astronomicalObject.commonName.isEmpty()) {
                holder.objectNameTextView.text = astronomicalObject.cataloguedName

            } else {
                holder.objectNameTextView.text = astronomicalObject.commonName
                holder.listImageView.setImageResource(R.drawable.stars)
            }

        } else if (astronomicalObject is DeepSkyObject) {

            holder.listImageView.setImageResource(astronomicalObject.type.imageResource)

            if (astronomicalObject.commonName.isEmpty()) {
                holder.objectNameTextView.text =
                    "${astronomicalObject.primaryCatalogName} ${astronomicalObject.primaryNumberID}"

            } else {
                holder.objectNameTextView.text = astronomicalObject.commonName

            }
        }

    }

    override fun getItemCount() = list.size

    inner class AstroHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var astronomicalObject: AstronomicalObject? = null

        val addToFavoritesCheckBox: ToggleButton = itemView.findViewById(R.id.addToFavoritesCheckBox)
        val positionInfoTextView: TextView = itemView.findViewById(R.id.positionInfoTextView)
        val listImageView: ImageView = itemView.findViewById(R.id.listImageView)
        val objectNameTextView: TextView = itemView.findViewById(R.id.objectNameTextView)
        val magnitudeTextView: TextView = itemView.findViewById(R.id.magnitudeTextView)

        init {
            addToFavoritesCheckBox.setOnClickListener {
                astronomicalObject?.let {
                    onFavoritesButtonClickListener?.invoke(it, addToFavoritesCheckBox.isChecked)
                }

            }
            Log.e("Astroholder", "Listeners created: ${++count}")

        }

    }




}