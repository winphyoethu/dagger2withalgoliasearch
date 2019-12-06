package com.rgo47.daggertest.adapter

import android.content.Context
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.rgo47.daggertest.R
import com.rgo47.daggertest.model.AlgoliaProductModel
import com.rgo47.daggertest.utils.inflate
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AlgoliaAdapter(val context: Context, val productListener: ProductClickListener) :
    PagedListAdapter<AlgoliaProductModel, AlgoliaAdapter.AlgoliaProductViewHolder>(
        AlgoliaAdapter
    ) {

    private val currentTimeInMilli = Calendar.getInstance().timeInMillis

//    private val preferenceHelper: PreferenceHelper by lazy {
//        PreferenceHelper(context)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlgoliaProductViewHolder {
        return AlgoliaProductViewHolder(parent.inflate(R.layout.item_product))
    }

    override fun onBindViewHolder(holder: AlgoliaProductViewHolder, position: Int) {
        if (getItem(position) != null) {
            val product = getItem(position)

            holder.tvProductTitle.text = product?.name_en

            if (product?.promotion_image != null && product.promotion_image.isNotEmpty() && product.promotion_image != "null") {
                holder.sdvProduct.setImageURI(Uri.parse(product.promotion_image))
            } else {
                holder.sdvProduct.setImageURI(Uri.parse(product!!.image_medium_url!!))
            }

            holder.tvProductPrice.text = product.regular_price.toString() + " " + product.currency!!

            var startDateInMilli = 0L
            var endDateInMilli = 0L

            if (product.discount_from != null) {
                try {
                    val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)

                    val date = format.parse(product.discount_from.split(" ")[0])
                    startDateInMilli = date.time

                    val date1 = format.parse(product.discount_to!!.split(" ")[0])
                    endDateInMilli = date1.time

                } catch (e: ParseException) {
                    e.printStackTrace()

                    startDateInMilli = 0L
                    endDateInMilli = 0L
                }
            }
        }
    }

    companion object : DiffUtil.ItemCallback<AlgoliaProductModel>() {
        override fun areItemsTheSame(
            oldItem: AlgoliaProductModel, newItem: AlgoliaProductModel
        ): Boolean {
            return oldItem::class == newItem::class
        }

        override fun areContentsTheSame(
            oldItem: AlgoliaProductModel, newItem: AlgoliaProductModel
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    inner class AlgoliaProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvProductTitle: TextView = itemView.findViewById(R.id.tvProductTitle)
        var tvProductPrice: TextView = itemView.findViewById(R.id.tvProductPrice)
        var sdvProduct: SimpleDraweeView = itemView.findViewById(R.id.sdvProduct)
        var cvItemProduct: CardView = itemView.findViewById(R.id.cvItemProduct)

        init {
            itemView.setOnClickListener {
                productListener.onProductClicked(getItem(layoutPosition)!!)
            }
        }

    }

    interface ProductClickListener {
        fun onProductClicked(algoliaProduct: AlgoliaProductModel)
    }

}