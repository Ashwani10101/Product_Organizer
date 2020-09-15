package com.ash.shopadminlogin

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.ash.shopadminlogin.adaptors.MainRecycleViewAdaptor
import com.ash.shopadminlogin.models.Product
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerviewAdaptor :MainRecycleViewAdaptor
    private lateinit var searchView: SearchView //SearchView query needed by fragment tab change listener
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar_MainActivity)

        initSpinner()
        initRecycleView()
    }

    private fun initRecycleView() {
        recyclerviewAdaptor = MainRecycleViewAdaptor()

        reycleviewMain.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        reycleviewMain.adapter = recyclerviewAdaptor



    }

    private fun initSpinner()
    {
        val spinner: Spinner = findViewById(R.id.spinnerCategorySelector)

        val spinnerData: ArrayList<String> = ArrayList()

        val all = "ATTA,MASALE,GRAINS,PULSES,RICE,DRY FRUITS,SWEETNERS,SATTO,OTHERS"
        val list = all.split(',')
        spinnerData.addAll(list)

        val adapter: ArrayAdapter<String> = object : ArrayAdapter<String>(
            applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            spinnerData
        )
        {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                return setCentered(super.getView(position, convertView, parent))!!
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                return setCentered(super.getDropDownView(position, convertView, parent))!!
            }

            private fun setCentered(view: View): View? {
                val textView = view.findViewById<View>(android.R.id.text1) as TextView
                textView.textAlignment = View.TEXT_ALIGNMENT_CENTER


                return view
            }
        }


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter








    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_activity_toolbar, menu)
       /* val item = menu!!.findItem(R.id.MainMenuSearchButton)
        searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener
        {
            override fun onQueryTextSubmit(query: String?): Boolean { return false }
            override fun onQueryTextChange(query: String?): Boolean
            {   //Coroutine added
                if (query.toString().isNotEmpty() || query.toString() != "")
                {
                    showToast(query.toString())
                    //currentFragment.recycleViewAdapter.filter.filter(query)
                } else
                {
//                    for (fragment in fragmentList)
//                    {
//                        fragment.value.recycleViewAdapter.filter.filter("")
//                    }
                }
                return false
            }
        })*/
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        return  when (item.itemId) {
            R.id.MainMenueAddCategory ->
            {

                showToast("Add Clicked!!!")

                showToast("Add Button Clicked!!")
                val product = Product()
                product.name ="Hello"
                product.price ="2000"
                product.category ="Atta"
                recyclerviewAdaptor.addProduct(product,0)
                return true
            }
            R.id.MainMenuRefresh ->
            {

                showToast("Refesh Clicked!!!")
                return true
            }
            R.id.MainMenuSORT ->
            {

                showToast("Sort Clicked!!!")
                return true
            }
            R.id.MainMenuSearchButton->
            {

                showToast("Search Clicked!!!")
                return true
            }

            else -> super.onOptionsItemSelected(item)



        }


    }


    private fun show(message:String)
    {
        Log.i("###",message)
    }
    private fun showToast(msg:String)
    {
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
    }
    private fun showSnakeBar(msg:String,color: Int)
    {
        val snackbar = Snackbar.make(reycleviewMain, msg, Snackbar.LENGTH_LONG)
        val sbView: View = snackbar.view
        sbView.setBackgroundColor(this.getColor(color))
        val textView = sbView.findViewById(R.id.snackbar_text) as TextView
        textView.textSize = 15f
        textView.setTextColor(Color.WHITE)
        snackbar.show()
    }

}