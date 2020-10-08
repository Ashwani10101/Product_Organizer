package com.ash.shopadminlogin.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductEntityDataAccessObject
{
    @Insert
    fun addNewProduct(productEntity: ProductEntity)

    @Query("select * from ProductEntity")
    fun products() :List<ProductEntity>

    @Query("DELETE FROM ProductEntity")
    fun deleteAll()

}

@Dao
interface CustomerEntityDataAccessObject
{
    @Insert
    fun addNewCustomer(productEntity: CustomerEntity)

    @Query("select * from CustomerEntity")
    fun customers() :List<CustomerEntity>

    @Query("DELETE FROM CustomerEntity")
    fun deleteAll()

}