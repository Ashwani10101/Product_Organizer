package com.ash.shopadminlogin.LocalDatabase


import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [(ProductEntity::class)] ,version = 1 )
abstract class MyDatabase : RoomDatabase()
{
    abstract fun productDao() : DataAccessObject
}