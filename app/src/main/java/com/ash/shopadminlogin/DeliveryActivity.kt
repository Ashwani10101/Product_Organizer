package com.ash.shopadminlogin

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ash.customerapp.models.CustomerOrder
import com.ash.shopadminlogin.adaptors.RecyclerviewAdaptorCustomerOrder
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_deliverys.*

class DeliveryActivity : AppCompatActivity()
{
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerviewAdaptorOrder: RecyclerviewAdaptorCustomerOrder

    companion object
    {
        private val firebaseDatabaseRefAllOrders = FirebaseDatabase.getInstance().reference.child("AllOrders")

    }
    private var customerCount = 0
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deliverys)
        initRecyclerview()
        try
        {
            Handler().postDelayed({ initFirebase() },500)
        }catch (e:Exception)
        {

        }

    }

    private fun initFirebase()
    {
        recyclerviewAdaptorOrder.removeAll()

        firebaseDatabaseRefAllOrders.addChildEventListener(object : ChildEventListener
        {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?)
            {
                val userProfile = snapshot.child("Profile")
                val orderRef = snapshot.child("Orders")

                val customerOrder = CustomerOrder()
                customerOrder.name = userProfile.child("name").value.toString()
                customerOrder.phone = userProfile.child("phone").value.toString()
                customerOrder.address = userProfile.child("address").value.toString()

               val orderDetails = StringBuilder()

                for (orders in orderRef.children )
                {
                    orderDetails.append(orders.value.toString())
                    orderDetails.append("\n")
                   // show(orders.value.toString())
                }
                customerOrder.orderDetails = orderDetails.toString()

                recyclerviewAdaptorOrder.addCustomer(customerOrder,0)
                customerCount++
                customerActivity_textviewOrderCount.text = customerCount.toString()


                /*val url = firebaseStorageRefAllProductImages.child("${product!!.key}.jpg").path
                 show(url)
                 firebaseStorageRefAllProductImages.child("${product.key}.jpg").downloadUrl.addOnCompleteListener {
                 val fileLink = it.result
                 show("URL : ${fileLink.toString()}")
                 product.image =  fileLink//bitmap//getBitmapFromURL(fileLink.toString())

                }*/
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?)
            {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot)
            {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?)
            {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError)
            {
                TODO("Not yet implemented")
            }
        })
    }

/*    val getData = object : ValueEventListener
    {
        override fun onDataChange(snapshot: DataSnapshot)
        {
            var sb = StringBuilder()
            for (i in snapshot.children)
            {
                val id = i.child("id").value
                val name = i.child("name").value
                val salary = i.child("salary").value
                sb.append("$id $name $salary \n")
            }
            textViewData.text = sb
        }
    }*/

    private fun initRecyclerview()
    {
        recyclerView = findViewById<RecyclerView>(R.id.customerActivity_recycleview)
        recyclerviewAdaptorOrder = RecyclerviewAdaptorCustomerOrder()
        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recyclerView.adapter = recyclerviewAdaptorOrder


    }


    private fun show(message: String)
    {
        Log.i("###", message)
    }


}