package com.choptius.spec.domain

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.choptius.spec.R
import com.choptius.spec.domain.AstroAdapter.AstroHolder
import com.choptius.spec.databinding.ObjectRowItemBinding
import com.choptius.spec.db.AstroDatabase

class AstroAdapter(private val context: Context) : RecyclerView.Adapter<AstroHolder>(),
    View.OnClickListener{

    private var list: List<AstronomicalObject> = emptyList()
    private val database: AstroDatabase = AstroDatabase.getInstance(context)

    constructor(context: Context, list: List<AstronomicalObject>) : this(context) {
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AstroHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.object_row_item, parent, false)
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

    //обрабатывает нажатия с itemView
    override fun onClick(v: View) {
        val astronomicalObject = v.tag as AstronomicalObject

        if (v.id == R.id.addToFavoritesCheckBox) {
            val checkBox = v as ToggleButton
            val isChecked = checkBox.isChecked
            astronomicalObject.isInFavorites = isChecked
            Thread { database.setIsInFavorites(astronomicalObject, isChecked) }.start()

        } else {
            if (astronomicalObject is DeepSkyObject) {
                Log.e(TAG, "${astronomicalObject.type.fullName} ${astronomicalObject.type.imageResource}")
            }

        }
    }


    inner class AstroHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val b = ObjectRowItemBinding.inflate(LayoutInflater.from(context))
        private val addToFavoritesCheckBox = itemView.findViewById<ToggleButton>(R.id.addToFavoritesCheckBox)
        private val positionInfoTextView = itemView.findViewById<TextView>(R.id.positionInfoTextView)
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

            } else if (astronomicalObject is DeepSkyObject){

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