package com.sais.deliveryapp.models

import android.os.Parcel
import android.os.Parcelable

data class Business(
	var owner: String = "",
	var name: String="",
	var type: String="",
	var location: String="",
	var till: Int= 0,
	var id: String=""
	): Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readString()!!,
		parcel.readString()!!,
		parcel.readString()!!,
		parcel.readString()!!,
		parcel.readInt(),
		parcel.readString()!!
		) {
	}

	override fun describeContents(): Int {
		TODO("Not yet implemented")
	}

	override fun writeToParcel(p0: Parcel, p1: Int) {
		p0.writeString(owner)
		p0.writeString(name)
		p0.writeString(type)
		p0.writeString(location)
		p0.writeInt(till)
		p0.writeString(id)
	}

	companion object CREATOR : Parcelable.Creator<Business> {
		override fun createFromParcel(parcel: Parcel): Business {
			return Business(parcel)
		}

		override fun newArray(size: Int): Array<Business?> {
			return arrayOfNulls(size)
		}
	}
}
