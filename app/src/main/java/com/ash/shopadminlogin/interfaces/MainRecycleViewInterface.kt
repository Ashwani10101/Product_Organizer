package com.ash.shopadminlogin.interfaces

import android.view.View
import com.ash.shopadminlogin.firebase.Product

interface MainRecycleViewInterface
{
    fun onCardClick(product: Product)
    fun onEditCardClick(product: Product)
    fun onDeleteCardClick(product: Product)

}
