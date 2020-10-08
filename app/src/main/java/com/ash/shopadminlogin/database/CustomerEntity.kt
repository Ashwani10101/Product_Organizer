package com.ash.shopadminlogin.database

import android.os.Parcel
import android.os.Parcelable

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity //Table
class CustomerEntity() :Parcelable
{
    @PrimaryKey
    var id :Int? = null
    @ColumnInfo //Column in table
    var name :String = "No Name"
    @ColumnInfo
    var phone :String = "No Number"
    @ColumnInfo
    var address :String = "No Address"
    @ColumnInfo
    var orderType :String ="No OrderType"
    @ColumnInfo
    var orderDetails :String="No OrderDetails"


    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        name = parcel.readString().toString()
        phone = parcel.readString().toString()
        address = parcel.readString().toString()
        orderType = parcel.readString().toString()
        orderDetails = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(phone)
        parcel.writeString(address)
        parcel.writeString(orderType)
        parcel.writeString(orderDetails)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CustomerEntity> {
        override fun createFromParcel(parcel: Parcel): CustomerEntity {
            return CustomerEntity(parcel)
        }

        override fun newArray(size: Int): Array<CustomerEntity?> {
            return arrayOfNulls(size)
        }
    }


}