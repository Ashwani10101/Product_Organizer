package com.ash.shopadminlogin.database


import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [(ProductEntity::class),(CustomerEntity::class)] ,version = 1 )
abstract class MyDatabase : RoomDatabase()
{
    abstract fun productDao() : ProductEntityDataAccessObject
    abstract fun customerDao() : CustomerEntityDataAccessObject
}