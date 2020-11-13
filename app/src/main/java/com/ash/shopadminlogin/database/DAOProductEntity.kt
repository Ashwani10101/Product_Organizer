package com.ash.shopadminlogin.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DAOProductEntity
{



    @Insert
    fun insert(productEntity: ProductEntity)

    @Update
    fun update(productEntity: ProductEntity)

    @Delete
    fun delete(productEntity: ProductEntity)

    @Query("DELETE from ProductEntity")
    fun deleteAllProducts()

    @Insert
    fun insertAll(productEntityList: List<ProductEntity>)

    //@Query("SELECT * FROM NoteEntity ORDER BY priority DESC ")
    @Query("SELECT * FROM ProductEntity")
    fun getAllProducts(): LiveData<List<ProductEntity>>



}
