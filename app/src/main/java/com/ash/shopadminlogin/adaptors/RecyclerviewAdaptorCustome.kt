package com.ash.shopadminlogin.adaptors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ash.shopadminlogin.database.CustomerEntity
import com.ash.shopadminlogin.R
import kotlin.collections.ArrayList


class RecyclerviewAdaptorCustomer() : RecyclerView.Adapter<RecyclerviewAdaptorCustomer.MyViewHolder>()
{


    private val customerList = ArrayList<CustomerEntity>()
    private var customerListAll = ArrayList<CustomerEntity>()// Used for filtering in search bar

    fun addCustomerList(CustomerEntityList: ArrayList<CustomerEntity>)
    {
        customerList.addAll(CustomerEntityList)
        customerListAll = ArrayList<CustomerEntity>().apply { addAll(CustomerEntityList) }
    }


    fun addCustomer(customer: CustomerEntity, position: Int)
    {
        customerList.add(position, customer)
        customerListAll.add(position, customer)
        notifyItemInserted(position)
    }

    fun deleteCustomer(customer: CustomerEntity, position: Int)
    {
        customerList.remove(customer)
        customerListAll.removeAt(position)

        //trimming
        customerList.trimToSize()
        customerListAll.trimToSize()

        notifyItemRemoved(position)
    }

    fun clear()
    {
        customerList.clear()
        customerListAll.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.customer_card, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        return holder.bindData(customerList[position])
    }

    override fun getItemCount(): Int
    {
        return customerList.size
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        private val customerName: TextView = view.findViewById<TextView>(R.id.textView_CustomerName)
        private val customerNumber: TextView = view.findViewById<TextView>(R.id.textView_CustomerNumber)
        private val customerAddress: TextView = view.findViewById<TextView>(R.id.textView_CustomerAddress)
        private val customerOrderType: TextView = view.findViewById<TextView>(R.id.textView_CustomerOrdertype)
        private val customerOrderDetails: TextView = view.findViewById<TextView>(R.id.textView_CustomerOrderDetails)

        fun bindData(customerEntity: CustomerEntity)
        {

            customerName.text = customerEntity.name
            customerNumber.text = customerEntity.phone
            customerAddress.text = customerEntity.address
            customerOrderType.text = customerEntity.orderType
            customerOrderDetails.text = customerEntity.orderDetails

        }

    }


}