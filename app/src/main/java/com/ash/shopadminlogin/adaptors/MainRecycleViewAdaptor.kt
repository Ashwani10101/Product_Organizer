package com.ash.shopadminlogin.adaptors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ash.shopadminlogin.R
import com.ash.shopadminlogin.models.Product

class MainRecycleViewAdaptor () : RecyclerView.Adapter<MainRecycleViewAdaptor.MyViewHolder>()
{


    private val productList = ArrayList<Product>()

    fun addProduct(product: Product,position: Int)
    {
        productList.add(product)
        notifyItemInserted(position)
    }

    fun deleteProduct(product: Product,position: Int)
    {
        productList.remove(product)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_card_product,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       return holder.bindData(productList[position])
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    inner class MyViewHolder (view:View) :RecyclerView.ViewHolder(view)
    {
        private val productImage: ImageView = view.findViewById<ImageView>(R.id.imageView_ProductImage)
        private val productName: TextView = view.findViewById<TextView>(R.id.textView_ProductName)
        private val productCategory: TextView = view.findViewById<TextView>(R.id.textView_ProductCategory)
        private val productPrice: TextView = view.findViewById<TextView>(R.id.textView_ProductPrice)

        fun bindData(product: Product)
        {
            //productImage.setImageBitmap(product.image)
            productName.text = product.name
            productCategory.text = product.category
            productPrice.text = product.price
        }
    }
}