package com.rgo47.daggertest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.helper.android.filter.facet.FacetListAdapter
import com.algolia.instantsearch.helper.android.filter.state.connectPagedList
import com.algolia.instantsearch.helper.android.list.SearcherSingleIndexDataSource
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxConnectorPagedList
import com.algolia.instantsearch.helper.filter.facet.FacetListConnector
import com.algolia.instantsearch.helper.filter.facet.FacetListPresenterImpl
import com.algolia.instantsearch.helper.filter.facet.FacetSortCriterion
import com.algolia.instantsearch.helper.filter.facet.connectView
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.instantsearch.helper.stats.StatsConnector
import com.algolia.search.client.ClientSearch
import com.algolia.search.dsl.query
import com.algolia.search.dsl.settings
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter
import com.algolia.search.transport.RequestOptions
import com.rgo47.daggertest.adapter.BrandFacetViewHolder
import com.rgo47.daggertest.adapter.MyFacetListViewHolder
import com.rgo47.daggertest.model.AlgoliaProductModel
import io.ktor.client.features.logging.LogLevel

class MainViewModel() : ViewModel() {

    val client = ClientSearch(
        ApplicationID("APPID"), APIKey("APIKEY"), LogLevel.ALL
    )
    val index = client.initIndex(IndexName("INDEX"))
    val setting = settings {
        hitsPerPage = 20
    }
    val query = query {
        hitsPerPage = 30
    }

    val requestOptioins = RequestOptions().parameter("status", "show")
    val searcher = SearcherSingleIndex(index, query)
    val connection = ConnectionHandler()
    val stats = StatsConnector(searcher)

    val filterState = FilterState()
    val facetList = FacetListConnector(
        searcher = searcher,
        filterState = filterState,
        attribute = Attribute("main_category_en"),
        selectionMode = SelectionMode.Multiple,
        groupID = FilterGroupID("main_category_en", FilterOperator.And)
    )
    val facetPresenter = FacetListPresenterImpl(
        sortBy = listOf(FacetSortCriterion.CountDescending, FacetSortCriterion.IsRefined),
        limit = 100
    )

    val facetList1 = FacetListConnector(
        searcher = searcher,
        filterState = filterState,
        attribute = Attribute("brand"),
        selectionMode = SelectionMode.Multiple,
        groupID = FilterGroupID("brand", FilterOperator.And)
    )
    val facetPresenter1 = FacetListPresenterImpl(
        sortBy = listOf(FacetSortCriterion.CountDescending, FacetSortCriterion.IsRefined),
        limit = 100
    )
    val adapterFacet = FacetListAdapter(MyFacetListViewHolder.Factory)
    val adapterFacet1 = FacetListAdapter(BrandFacetViewHolder.Factory)

    val dataSourceFactory = SearcherSingleIndexDataSource.Factory(searcher) { hit ->
        AlgoliaProductModel(
            hit.json.getPrimitive("id").int,
            hit.json.getPrimitive("name_en").content,
            hit.json.getPrimitive("name_mm").content,
            hit.json.getPrimitive("sku").content,
            hit.json.getPrimitive("description_mm").content,
            hit.json.getPrimitive("description_en").content,
            hit.json.getPrimitive("regular_price").int,
            hit.json.getPrimitive("discount_price").content,
            hit.json.getPrimitive("image_origin_url").content,
            hit.json.getPrimitive("image_medium_url").content,
            hit.json.getPrimitive("currency").content,
            hit.json.getPrimitive("brand").content,
            hit.json.getPrimitive("discount_from").content,
            hit.json.getPrimitive("discount_to").content,
            hit.json.getPrimitive("status").content,
            hit.json.getPrimitive("deleted_at").content,
            hit.json.getPrimitive("promotion_image").content
        )
    }

    val pagedListConfig = PagedList.Config.Builder().setPageSize(3).build()
    val products: LiveData<PagedList<AlgoliaProductModel>> =
        LivePagedListBuilder<Int, AlgoliaProductModel>(dataSourceFactory, pagedListConfig).build()

    val searchBox = SearchBoxConnectorPagedList(searcher, listOf(products))

    val facetGroup = FilterGroupID("status", FilterOperator.And)
    val facet = Filter.Facet(Attribute("status"), "show", null, false)

    init {
//        filterState.filters.value.getFacetGroups().map {
//            FilterGroup.And.Facet(facet)
//        }
//        val map: MutableMap<FilterGroupID, Set<Filter>> = HashMap()
//        val set: MutableSet<Filter.Facet> = HashSet()
//        set.add(facet)
//        map.put(facetGroup, set)
//        val state = FilterState(map)
//        state = FilterState(map)
        filterState.add(facetGroup, facet)

//        Timber.tag("STATEGG :: ")
//            .d(filterState.filters.value.getFacetGroups().toString() + " hello")

        connection += searchBox

        connection += facetList
        connection += facetList1

        connection += filterState.connectPagedList(products)
        connection += searcher.connectFilterState(filterState)
        connection += facetList.connectView(adapterFacet, facetPresenter)
        connection += facetList1.connectView(adapterFacet1, facetPresenter1)

        searcher.searchAsync()
    }

    override fun onCleared() {
        super.onCleared()
        searcher.cancel()
        connection.disconnect()
    }

//    val testStrMutable = MutableLiveData<String>()
//
//    val testStr: LiveData<String>
//        get() = testStrMutable
//
//    fun getDataFromRepo() {
//        testStrMutable.value = "SHIT"
//    }

}