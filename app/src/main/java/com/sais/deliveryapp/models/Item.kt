package com.sais.deliveryapp.models

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

data class Item (
	var id: String="",
	var bsId: String ="",
	var title: String  ="",
	var bsName: String = "",
	var price: Int = 0,
	var quantity: String = "",
	var description: String = "",
	var category: String = "",
	var image: String ="",
//	var discount: Boolean= false,
//	var discountAmount: Int = 0,
//	var delivery: Boolean = false,
//	var deliveryFee : Int = 0
	): Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readString()!!,
		parcel.readString()!!,
		parcel.readString()!!,
		parcel.readString()!!,
		parcel.readInt(),
		parcel.readString()!!,
		parcel.readString()!!,
		parcel.readString()!!,
		parcel.readString()!!
	) {
	}

	override fun describeContents(): Int {
		TODO("Not yet implemented")
	}

	override fun writeToParcel(p0: Parcel, p1: Int) {
		p0.writeString(id)
		p0.writeString(bsId)
		p0.writeString(title)
		p0.writeString(bsName)
		p0.writeInt(price)
		p0.writeString(category)
		p0.writeString(quantity)
		p0.writeString(description)
		p0.writeString(image)
	}

	companion object CREATOR : Parcelable.Creator<Item> {
		override fun createFromParcel(parcel: Parcel): Item {
			return Item(parcel)
		}

		override fun newArray(size: Int): Array<Item?> {
			return arrayOfNulls(size)
		}
	}
}