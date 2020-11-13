package com.ash.shopadminlogin.adaptors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.ash.shopadminlogin.R
import com.ash.shopadminlogin.database.ProductEntity
import java.util.*
import kotlin.collections.ArrayList


class MainRecycleViewAdaptor : RecyclerView.Adapter<MainRecycleViewAdaptor.MyViewHolder>(), Filterable
{


    private val productList = ArrayList<ProductEntity>()
    private var productListAll = ArrayList<ProductEntity>()// Used for filtering in search bar

    fun addProductList(productEntityList: ArrayList<ProductEntity>)
    {
        productList.addAll(productEntityList)
        productListAll.addAll(productEntityList) //= ArrayList<ProductEntity>().apply { addAll(productEntityList) }
        notifyDataSetChanged()
    }

    fun addProduct(product: ProductEntity, position: Int)
    {
        productList.add(position, product)
        productListAll.add(position, product)
        notifyItemInserted(position)
    }

    fun deleteProduct(product: ProductEntity, position: Int)
    {
        productList.remove(product)
        productListAll.removeAt(position)

        //trimming
        productList.trimToSize()
        productListAll.trimToSize()

        notifyItemRemoved(position)
    }

    fun clear()
    {
        productList.clear()
        productListAll.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_card_product, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        return holder.bindData(productList[position])
    }

    override fun getItemCount(): Int
    {
        return productList.size
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        private val productImage: ImageView = view.findViewById<ImageView>(R.id.imageView_ProductImage)
        private val productName: TextView = view.findViewById<TextView>(R.id.textView_ProductName)
        private val productDetails: TextView = view.findViewById<TextView>(R.id.textView_ProductDetails)
        private val productCategory: TextView = view.findViewById<TextView>(R.id.textView_ProductCategory)
        private val productPrice: TextView = view.findViewById<TextView>(R.id.textView_ProductPrice)

        fun bindData(product: ProductEntity)
        {
            if (product.image != null)
            {
                productImage.setImageBitmap(product.image)
            } else
            {
                productImage.setImageResource(R.drawable.no_image_selected)
            }

            productName.text = product.name
            productDetails.text = product.details
            productPrice.text = product.price
            productCategory.text = product.category

        }
    }

    override fun getFilter(): Filter
    {
        return filter()
    }


    private fun filter(): Filter
    {
        return object : Filter()
        {
            //Run in background thread
            override fun performFiltering(charSequence: CharSequence?): FilterResults
            {
                val filterList = ArrayList<ProductEntity>()


                if (charSequence == null || charSequence.toString().isEmpty() || charSequence.toString() == "")
                {
                    filterList.addAll(productListAll)
                } else
                {
                    for (productEntity in productListAll)
                    {
                        if (productEntity.name.toLowerCase().trim().contains(charSequence.toString().toLowerCase().trim()) || productEntity.details.toLowerCase().trim().contains(charSequence.toString().toLowerCase().trim()))
                        {
                            filterList.add(productEntity)
                        }

                        if (productEntity.category.toLowerCase().trim().contains(charSequence.toString().toLowerCase().trim()))
                        {
                            filterList.add(productEntity)
                        }


                    }
                }

                val filterResult = FilterResults()
                filterResult.values = filterList

                return filterResult
            }

            //Run in UI thread
            override fun publishResults(constraint: CharSequence?, filterResult: FilterResults?)
            {
                productList.clear()
                productList.addAll(filterResult!!.values as Collection<ProductEntity>)
                notifyDataSetChanged()
            }
        }
    }


}