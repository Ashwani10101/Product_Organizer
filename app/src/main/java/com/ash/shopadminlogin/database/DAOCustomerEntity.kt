package com.ash.shopadminlogin.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DAOCustomerEntity
{
    @Insert
    fun insert(customerEntity: CustomerEntity)

    @Update
    fun update(customerEntity: CustomerEntity)

    @Delete
    fun delete(customerEntity: CustomerEntity)

    @Query("DELETE from CustomerEntity")
    fun deleteAllCustomers()

    //@Query("SELECT * FROM NoteEntity ORDER BY priority DESC ")
    @Query("SELECT * FROM CustomerEntity")
    fun getAllCustomer(): LiveData<List<CustomerEntity>>

}