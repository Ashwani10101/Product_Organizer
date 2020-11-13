package com.ash.shopadminlogin.database

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyRepository(application: Application)
{
    private var productDao: DAOProductEntity
    private var customerDao: DAOCustomerEntity

    private var allProducts: LiveData<List<ProductEntity>>
    private var allCustomers: LiveData<List<CustomerEntity>>

    //private var allNotes :LiveData<List<NoteEntity>>

    init
    {
        val database = MyDatabase.getInstance(application)
        productDao = database.productDao()
        customerDao = database.acceptedOrderDao()
        allProducts = productDao.getAllProducts()
        allCustomers = customerDao.getAllCustomer()
    }

    fun addProduct(productEntity: ProductEntity)
    {
        CoroutineScope(Dispatchers.IO).launch {
            productDao.insert(productEntity)
        }
    }

    fun updateProduct(productEntity: ProductEntity)
    {
        CoroutineScope(Dispatchers.IO).launch {
            productDao.update(productEntity)
        }
    }

    fun removeProduct(productEntity: ProductEntity)
    {
        CoroutineScope(Dispatchers.IO).launch {
            productDao.delete(productEntity)
        }
    }

    fun deleteAllProducts()
    {
        CoroutineScope(Dispatchers.IO).launch {
            productDao.deleteAllProducts()
        }
    }

    fun insertAllProducts(productEntityList :List<ProductEntity>)
    {
        CoroutineScope(Dispatchers.IO).launch {
            productDao.insertAll(productEntityList)
        }
    }

    fun getAllProducts(): LiveData<List<ProductEntity>>?
    {
        return allProducts
    }


    fun addCustomer(customerEntity: CustomerEntity)
    {
        CoroutineScope(Dispatchers.IO).launch {
            customerDao.insert(customerEntity)
        }
    }

    fun updateCustomer(customerEntity: CustomerEntity)
    {
        CoroutineScope(Dispatchers.IO).launch {
            customerDao.update(customerEntity)
        }
    }

    fun removeCustomer(customerEntity: CustomerEntity)
    {
        CoroutineScope(Dispatchers.IO).launch {
            customerDao.delete(customerEntity)
        }
    }

    fun deleteAllCustomers()
    {
        CoroutineScope(Dispatchers.IO).launch {
            customerDao.deleteAllCustomers()
        }
    }

    fun getAllCustomers(): LiveData<List<CustomerEntity>>?
    {
        return allCustomers
    }


}