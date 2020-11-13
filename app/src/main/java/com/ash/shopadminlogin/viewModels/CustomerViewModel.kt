package com.ash.shopadminlogin.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ash.shopadminlogin.database.MyRepository
import com.ash.shopadminlogin.database.CustomerEntity

class CustomerViewModel(application: Application) : AndroidViewModel(application)
{
    private var repository :MyRepository = MyRepository(application)
    private var allCustomers : LiveData<List<CustomerEntity>>? = null

    init {

        allCustomers = repository.getAllCustomers()
    }

    fun addCustomer(customerEntity: CustomerEntity)
    {
        repository.addCustomer(customerEntity)
    }
    fun updateCustomer(customerEntity: CustomerEntity)
    {
        repository.updateCustomer(customerEntity)
    }
    fun removeCustomer(customerEntity: CustomerEntity)
    {
        repository.removeCustomer(customerEntity)
    }

    fun deleteAllCustomers()
    {
        repository.deleteAllCustomers()
    }

    fun getAllCustomers() : LiveData<List<CustomerEntity>>?
    {
        return allCustomers
    }


}