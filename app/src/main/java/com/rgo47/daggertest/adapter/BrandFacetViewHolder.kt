package com.rgo47.daggertest.adapter

import android.view.View
import android.view.ViewGroup
import com.algolia.instantsearch.helper.android.filter.facet.FacetListViewHolder
import com.algolia.search.model.search.Facet
import com.rgo47.daggertest.R
import com.rgo47.daggertest.utils.inflate
import com.rgo47.daggertest.utils.invisible
import com.rgo47.daggertest.utils.visible
import kotlinx.android.synthetic.main.item_facet.view.*

class BrandFacetViewHolder(view: View) : FacetListViewHolder(view)  {

    object Factory : FacetListViewHolder.Factory {
        override fun createViewHolder(parent: ViewGroup): FacetListViewHolder {
            return MyFacetListViewHolder(parent.inflate(R.layout.item_facet))
        }
    }

    override fun bind(facet: Facet, selected: Boolean, onClickListener: View.OnClickListener) {
        view.setOnClickListener(onClickListener)
        view.facetCount.text = facet.count.toString()
        view.facetCount.visibility = View.VISIBLE
        if (selected) {
            view.icon.visible()
//            view.facetCount.setTypeface(null, Typeface.BOLD)
//            view.facetName.setTypeface(null, Typeface.BOLD)
        } else {
            view.icon.invisible()
//            view.facetCount.setTypeface(null, Typeface.NORMAL)
//            view.facetName.setTypeface(null, Typeface.NORMAL)
        }
        view.facetName.text = facet.value
    }

}