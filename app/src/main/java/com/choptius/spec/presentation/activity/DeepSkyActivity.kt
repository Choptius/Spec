package com.choptius.spec.presentation.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.choptius.spec.R
import com.choptius.spec.databinding.ActivityDeepskyBinding
import com.choptius.spec.db.AstroDatabase
import com.choptius.spec.domain.entities.AstronomicalObject
import com.choptius.spec.presentation.adapter.AstroAdapter
import com.choptius.spec.presentation.viewmodel.DeepSkyViewModel

class DeepSkyActivity : AppCompatActivity() {

    private lateinit var database: AstroDatabase
    private lateinit var adapter: AstroAdapter
    private lateinit var b: ActivityDeepskyBinding
    private lateinit var viewModel: DeepSkyViewModel

    private val catalogsArray = arrayOf("M", "NGC", "IC")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDeepskyBinding.inflate(layoutInflater)
        setContentView(b.root)

        database = AstroDatabase.getInstance(this)
        viewModel = ViewModelProvider(this).get(DeepSkyViewModel::class.java)

        b.catalogsSpinner.adapter =
            ArrayAdapter.createFromResource(this, R.array.catalogs, R.layout.spinner_item).apply {
                this.setDropDownViewResource(R.layout.dropdown_spinner_item)
            }

        b.catalogsSpinner.onItemSelectedListener =

            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) = updateRecyclerView()

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }


        adapter = AstroAdapter()
        adapter.onFavoritesButtonClickListener = { obj, isChecked ->

            if (isChecked) {
                viewModel.addToFavorites(obj)
            } else {
                viewModel.deleteFromFavorites(obj)
            }
        }

        b.objectsList.adapter = adapter

        viewModel.deepSkyObjectsList.observe(this) {
            adapter.list = it
            b.objectsList.scrollToPosition(0)
        }

        b.enterNumber.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(number: CharSequence, start: Int, before: Int, count: Int) =
                updateRecyclerView()

            override fun afterTextChanged(s: Editable) {}
        })
    }

    //поиск объектов из базы данных и заполнение ими RecyclerView по имени каталога и номеру в каталоге
    private fun updateRecyclerView() {
        val catalog = catalogsArray[b.catalogsSpinner.selectedItemPosition]
        val number: CharSequence = b.enterNumber.text
        Log.e("UpdateRecyclerView", "$catalog $number")
        viewModel.getDeepSkyObjects(catalog, number)

    }
}