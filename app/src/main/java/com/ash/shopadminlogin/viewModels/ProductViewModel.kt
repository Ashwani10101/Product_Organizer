package com.ash.shopadminlogin.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ash.shopadminlogin.database.MyRepository
import com.ash.shopadminlogin.database.ProductEntity

class ProductViewModel(application: Application) : AndroidViewModel(application)
{
    private var repository :MyRepository = MyRepository(application)
    private var allProducts : LiveData<List<ProductEntity>>? = null

    init {

        allProducts = repository.getAllProducts()
    }

    fun addProduct(productEntity: ProductEntity)
    {
        repository.addProduct(productEntity)
    }
    fun updateProduct(productEntity: ProductEntity)
    {
        repository.updateProduct(productEntity)
    }
    fun removeProduct(productEntity: ProductEntity)
    {
        repository.removeProduct(productEntity)
    }

    fun deleteAllProducts()
    {
        repository.deleteAllProducts()
    }
    fun insertAllProducts(productEntityList : List<ProductEntity>)
    {
        repository.insertAllProducts(productEntityList)
    }

    fun getAllProducts() : LiveData<List<ProductEntity>>?
    {
        return allProducts
    }


}