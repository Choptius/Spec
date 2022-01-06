package com.choptius.spec.presentation.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.choptius.spec.R
import com.choptius.spec.presentation.adapter.AstroAdapter.AstroHolder
import com.choptius.spec.domain.entities.AstronomicalObject
import com.choptius.spec.domain.entities.DeepSkyObject
import com.choptius.spec.domain.entities.Star

class AstroAdapter : RecyclerView.Adapter<AstroHolder>(), View.OnClickListener {

    private var list: List<AstronomicalObject> = emptyList()
    var onFavoritesButtonClickListener: ((obj: AstronomicalObject, isChecked: Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AstroHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.object_item, parent, false)
        return AstroHolder(view)
    }

    override fun onBindViewHolder(holder: AstroHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list: List<AstronomicalObject>) {
        this.list = list
        notifyDataSetChanged()
    }

    //обрабатывает нажатия с itemView(с favoritesCheckBox в том числе)
    override fun onClick(v: View) {
        val astronomicalObject = v.tag as AstronomicalObject

        if (v.id == R.id.addToFavoritesCheckBox) {
            val toggleButton = v.findViewById<ToggleButton>(R.id.addToFavoritesCheckBox)
            astronomicalObject.isInFavorites = toggleButton.isChecked
            onFavoritesButtonClickListener?.invoke(astronomicalObject, toggleButton.isChecked)

        }
    }


    inner class AstroHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val addToFavoritesCheckBox =
            itemView.findViewById<ToggleButton>(R.id.addToFavoritesCheckBox)
        private val positionInfoTextView =
            itemView.findViewById<TextView>(R.id.positionInfoTextView)
        private val listImageView = itemView.findViewById<ImageView>(R.id.listImageView)
        private val objectNameTextView = itemView.findViewById<TextView>(R.id.objectNameTextView)
        private val magnitudeTextView = itemView.findViewById<TextView>(R.id.magnitudeTextView)

        init {
            itemView.setOnClickListener(this@AstroAdapter)
            addToFavoritesCheckBox.setOnClickListener(this@AstroAdapter)
        }

        fun bind(astronomicalObject: AstronomicalObject) {

            addToFavoritesCheckBox.tag = astronomicalObject
            itemView.tag = astronomicalObject

            //связываю с общими полями AstronomicalObject

            if (astronomicalObject.magnitude != 0.0) {
                magnitudeTextView.text = "Magnitude: ${astronomicalObject.magnitude}"
            }
            positionInfoTextView.text = astronomicalObject.constellation.fullName
            addToFavoritesCheckBox.isChecked = astronomicalObject.isInFavorites

            //связываю с полями Star или DeepSkyObject
            if (astronomicalObject is Star) {

                if (astronomicalObject.commonName.isEmpty()) {
                    objectNameTextView.text = astronomicalObject.cataloguedName

                } else {
                    objectNameTextView.text = astronomicalObject.commonName
                    listImageView.setImageResource(R.drawable.stars)
                }

            } else if (astronomicalObject is DeepSkyObject) {

                listImageView.setImageResource(astronomicalObject.type.imageResource)

                if (astronomicalObject.commonName.isEmpty()) {
                    objectNameTextView.text =
                        "${astronomicalObject.primaryCatalogName} ${astronomicalObject.primaryNumberID}"

                } else {
                    objectNameTextView.text = astronomicalObject.commonName

                }
            }
        }


    }

    companion object {
        const val TAG = "AstroAdapter"
    }

}