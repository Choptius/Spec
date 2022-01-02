package com.choptius.spec

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.choptius.spec.R
import com.choptius.spec.adapters.AstroAdapter
import com.choptius.spec.databinding.ActivityDeepskyBinding
import com.choptius.spec.db.AstroDatabase

class DeepSkyActivity : AppCompatActivity() {


    private lateinit var database: AstroDatabase
    private lateinit var adapter: AstroAdapter
    private lateinit var b: ActivityDeepskyBinding

    private val catalogsArray = arrayOf("M", "NGC", "IC")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDeepskyBinding.inflate(layoutInflater)
        setContentView(b.root)

        database = AstroDatabase.getInstance(this)

        val arrayAdapter =
            ArrayAdapter.createFromResource(this, R.array.catalogs, R.layout.spinner_item)
        arrayAdapter.setDropDownViewResource(R.layout.dropdown_spinner_item)
        b.catalogsSpinner.adapter = arrayAdapter

        b.catalogsSpinner.onItemSelectedListener =

            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) = updateRecyclerView()

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }


        b.objectsList.layoutManager = LinearLayoutManager(this)
        b.objectsList.scrollToPosition(0)
        adapter = AstroAdapter(
            this, database.getDeepSkyObjects(
                catalogsArray[b.catalogsSpinner.selectedItemPosition], ""
            )
        )
        b.objectsList.adapter = adapter

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

        when(catalog) {
            "dkjvh","dkjfhk" -> TODO()
        }

        Thread {
            val list = database.getDeepSkyObjects(catalog, number)
            Handler(mainLooper).post {
                adapter.setList(list)
                b.objectsList.scrollToPosition(0)
            }
        }.start()
    }
}