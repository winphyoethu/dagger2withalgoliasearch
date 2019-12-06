package com.rgo47.daggertest

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.helper.android.list.autoScrollToStart
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.android.searchbox.connectView
import com.rgo47.daggertest.adapter.AlgoliaAdapter
import com.rgo47.daggertest.model.AlgoliaProductModel
import com.rgo47.daggertest.utils.gone
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), AlgoliaAdapter.ProductClickListener {

    private val algoliaPagerAdapter = AlgoliaAdapter(this, this)

    private val connection = ConnectionHandler()

    override fun onDestroy() {
        System.gc()
        connection.disconnect()
        super.onDestroy()
    }


    fun initViewAndVariables() {

        viewModel.products.observe(this, Observer {
            algoliaPagerAdapter.submitList(it)
        })

        rvAlgoliaProduct.let {
            it.itemAnimator = null
            it.adapter = algoliaPagerAdapter
            it.layoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL)
            it.autoScrollToStart(algoliaPagerAdapter)
        }

        facetList.let {
            it.adapter = viewModel.adapterFacet
            it.layoutManager = LinearLayoutManager(this@MainActivity)
            it.autoScrollToStart(viewModel.adapterFacet)
        }

        facetList1.let {
            it.adapter = viewModel.adapterFacet1
            it.layoutManager = LinearLayoutManager(this@MainActivity)
            it.autoScrollToStart(viewModel.adapterFacet1)
        }

        rvAlgoliaProduct.setOnClickListener {
            dlAlgoliaSearch.openDrawer(GravityCompat.END)
        }

        val searchBoxView = SearchBoxViewAppCompat(svAlgolia)

        svAlgolia.setIconifiedByDefault(false)
        svAlgolia.clearFocus()

        val magImage = svAlgolia.findViewById(R.id.search_mag_icon) as ImageView
        magImage.gone()
        magImage.setImageDrawable(null)

        val textView = svAlgolia.findViewById(R.id.search_src_text) as TextView
        textView.setTextColor(Color.BLACK)
        textView.setHintTextColor(Color.parseColor("#A5D6A7"))
        textView.hint = " Search"
        textView.textSize = 14f

        connection += viewModel.searchBox.connectView(searchBoxView)
    }

    @Inject
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        (application as App)
//            .appComponent
//            .inject(this)

//        tvGG.setOnClickListener {
//            viewModel.getDataFromRepo()
//        }
//
//        viewModel.testStr.observe(this, Observer {
//            tvGG.text = it
//        })

        initViewAndVariables()

    }

    override fun onProductClicked(algoliaProduct: AlgoliaProductModel) {

    }
}
