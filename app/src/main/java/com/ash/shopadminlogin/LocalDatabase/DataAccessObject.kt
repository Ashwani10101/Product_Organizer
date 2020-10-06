package com.ash.shopadminlogin.LocalDatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DataAccessObject
{
    @Insert
    fun addNewProduct(productEnitity: ProductEntity)

    @Query("select * from ProductEntity")
    fun products() :List<ProductEntity>

    @Query("DELETE FROM ProductEntity")
    fun deleteAll()

}