package com.ash.shopadminlogin.LocalDatabase

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity //Table
class ProductEntity() :Parcelable
{
    @PrimaryKey
    var id :Int? = null
    @ColumnInfo //Column in table
    var name :String = ""
    @ColumnInfo
    var price :String = ""
    @ColumnInfo
    var details :String = ""
    @ColumnInfo
    var category :String =""
    @ColumnInfo
    var imageID :String? = null


    @Ignore
    var image: Bitmap? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        name = parcel.readString().toString()
        price = parcel.readString().toString()
        details = parcel.readString().toString()
        category = parcel.readString().toString()
        imageID = parcel.readString()
        image = parcel.readParcelable(Bitmap::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(price)
        parcel.writeString(details)
        parcel.writeString(category)
        parcel.writeString(imageID)
        parcel.writeParcelable(image, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductEntity> {
        override fun createFromParcel(parcel: Parcel): ProductEntity {
            return ProductEntity(parcel)
        }

        override fun newArray(size: Int): Array<ProductEntity?> {
            return arrayOfNulls(size)
        }
    }


}