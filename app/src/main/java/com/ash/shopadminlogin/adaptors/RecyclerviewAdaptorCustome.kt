package com.ash.shopadminlogin.adaptors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ash.customerapp.models.CustomerOrder
import com.ash.shopadminlogin.R
import kotlin.collections.ArrayList


class RecyclerviewAdaptorCustomerOrder() : RecyclerView.Adapter<RecyclerviewAdaptorCustomerOrder.MyViewHolder>()
{
    
    

    private val customerList = ArrayList<CustomerOrder>()
    private var customerListAll = ArrayList<CustomerOrder>()// Used for filtering in search bar

    fun addCustomerList(customerOrderList: ArrayList<CustomerOrder>)
    {
        customerList.addAll(customerOrderList)
        customerListAll = ArrayList<CustomerOrder>().apply { addAll(customerOrderList) }
    }


    fun addCustomer(customer: CustomerOrder, position: Int)
    {
        customerList.add(position, customer)
        customerListAll.add(position, customer)
        notifyItemInserted(position)
    }

    fun deleteCustomer(customer: CustomerOrder, position: Int)
    {
        customerList.remove(customer)
        customerListAll.removeAt(position)

        //trimming
        customerList.trimToSize()
        customerListAll.trimToSize()

        notifyItemRemoved(position)
    }

    fun removeAll()
    {
        customerList.clear()
        customerListAll.clear()
        notifyDataSetChanged()
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
        //private val customerOrderType: TextView = view.findViewById<TextView>(R.id.textView_CustomerOrdertype)
       private val customerOrderDetails: TextView = view.findViewById<TextView>(R.id.textView_CustomerOrderDetails)

        fun bindData(customerEntity: CustomerOrder)
        {

            customerName.text = customerEntity.name
            customerNumber.text = customerEntity.phone
            customerAddress.text = customerEntity.address
            customerOrderDetails.text = customerEntity.orderDetails

        }

    }


}