package com.ash.shopadminlogin

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ash.shopadminlogin.adaptors.RecyclerviewAdaptorCustomer
import com.ash.shopadminlogin.database.CustomerEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class CustomerActivity : AppCompatActivity()
{
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdaptor: RecyclerviewAdaptorCustomer
    private lateinit var btnAdd: Button
    private lateinit var btnRemove: Button

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer)
        initLayout()

    }

    private fun initRecycleview()
    {
        CoroutineScope(Dispatchers.IO).launch {
            val c = MainActivity.myDatabase!!.customerDao().customers()
            recyclerAdaptor.addCustomerList(ArrayList(c))

            CoroutineScope(Dispatchers.Main).launch {
                recyclerAdaptor.notifyDataSetChanged()
            }
        }



    }

    var count = 0
    private fun initLayout()
    {
        recyclerView = findViewById<RecyclerView>(R.id.customerActivity_recycleview)
        btnAdd  = findViewById<Button>(R.id.customerActivity_btnAdd)
        btnRemove = findViewById<Button>(R.id.customerActivity_btnRemove)

        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recyclerAdaptor = RecyclerviewAdaptorCustomer()
        recyclerView.adapter = recyclerAdaptor

        btnAdd.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                val customer = CustomerEntity()
                customer.name = "Name $count"
                customer.phone = getRandomPhoneNumber()
                customer.address = "Address : 244, A-Block, Sector-50, Noida, U.P., INDIA, EARTH(3rd), Sun(C-137), Orion Arm, Milky Way,Virgo Super-cluster, Laniakea Mega-cluster"
                customer.orderType = "Home Delivery"
                customer.orderDetails = "20kg Aashirvaad atta[1],200ml Coconut oil[1]"
                MainActivity.myDatabase!!.customerDao().addNewCustomer(customer)

                recyclerAdaptor.addCustomer(customer,recyclerAdaptor.itemCount)
            }

        }

        btnRemove.setOnClickListener {

        }

        initRecycleview()
    }

    private fun getRandomPhoneNumber(): String
    {
        val long = Random.nextLong(10_00_00_00_00,99_99_99_99_99)
        return  long.toString()

    }

}