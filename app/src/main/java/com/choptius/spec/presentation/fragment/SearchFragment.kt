package com.choptius.spec.presentation.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.choptius.spec.R
import com.choptius.spec.databinding.DeepskyFragmentBinding
import com.choptius.spec.presentation.adapter.AstroAdapter
import com.choptius.spec.presentation.viewmodel.DeepSkyViewModel


class SearchFragment : Fragment() {

    private val adapter: AstroAdapter by lazy {
        AstroAdapter()
    }
    private val viewModel: DeepSkyViewModel by lazy {
         ViewModelProvider(this)[DeepSkyViewModel::class.java]
    }

    private val b: DeepskyFragmentBinding by lazy {
        DeepskyFragmentBinding.inflate(layoutInflater)
    }

    private val catalogsArray = arrayOf("M", "NGC", "IC")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinner()
        setupRecyclerView()
        setupEditText()
    }

    private fun setupEditText() {
        b.enterNumber.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(number: CharSequence, start: Int, before: Int, count: Int) =
                updateRecyclerView()

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun setupRecyclerView() {

        adapter.onFavoritesButtonClickListener = { obj, isChecked ->

            if (isChecked) {
                viewModel.addToFavorites(obj)
            } else {
                viewModel.deleteFromFavorites(obj)
            }
        }

        b.objectsList.adapter = adapter

        viewModel.deepSkyObjectsList.observe(viewLifecycleOwner) {
            adapter.list = it
            b.objectsList.scrollToPosition(0)
        }

    }

    private fun setupSpinner() {

        context?.let {
            b.catalogsSpinner.adapter =
                ArrayAdapter.createFromResource(it, R.array.catalogs, R.layout.spinner_item).apply {
                    this.setDropDownViewResource(R.layout.dropdown_spinner_item)
                }

            b.catalogsSpinner.onItemSelectedListener =

                object : AdapterView.OnItemSelectedListener {

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View,
                        position: Int,
                        id: Long
                    ) = updateRecyclerView()

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
        }

    }

    //поиск объектов из базы данных и заполнение ими RecyclerView по имени каталога и номеру в каталоге
    private fun updateRecyclerView() {
        val catalog = catalogsArray[b.catalogsSpinner.selectedItemPosition]
        val number: CharSequence = b.enterNumber.text
        Log.e("UpdateRecyclerView", "$catalog $number")
        viewModel.getDeepSkyObjects(catalog, number)

    }
}