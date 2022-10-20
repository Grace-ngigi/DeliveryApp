package com.sais.deliveryapp.models

import android.os.Parcel
import android.os.Parcelable

data class Retailer(
	var id: String = "",
	val contact: Int= 0,
	val name: String=""
): Parcelable{
	constructor(parcel: Parcel) : this(
		parcel.readString()!!,
		parcel.readInt(),
		parcel.readString()!!
	) {
	}

	override fun describeContents(): Int {
		TODO("Not yet implemented")
	}

	override fun writeToParcel(p0: Parcel, p1: Int) {
		p0.writeString(id)
		p0.writeInt(contact)
		p0.writeString(name)

	}

	companion object CREATOR : Parcelable.Creator<Retailer> {
		override fun createFromParcel(parcel: Parcel): Retailer {
			return Retailer(parcel)
		}

		override fun newArray(size: Int): Array<Retailer?> {
			return arrayOfNulls(size)
		}
	}

}
