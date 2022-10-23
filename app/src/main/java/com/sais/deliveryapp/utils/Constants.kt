package com.sais.deliveryapp.utils

import android.app.Activity
import android.net.Uri
import android.webkit.MimeTypeMap

object Constants {
	const val EXTRA_RETAILER_INFO= "retailer"
	const val RETAILERS = "Retailers"

	const val EXTRA_BUSINESS_PROFILE = "business_profile"
	const val BUSINESS_PROFILE = "Business Owners"

	const val EXTRA_BUSINESS = "business"
	const val BUSINESS = "Business"
	const val ADDED_BUSINESS= "business"

	const val EXTRA_ITEM_INFO = "items"
	const val ITEMS = "Items"

	const val EXTRA_CART_ITEM = "items"
	const val CART_ITEM = " Cart Items"

	const val LOGO_GALLERY = 1
	const val ITEM_GALLERY = 2
	const val ADD_BS_REQUEST = 3
	const val UPDATE_BS_REQUEST = 4
	const val LOGIN_USER = 5
	const val ADD_ITEM_REQUEST = 6
	const val UPDATE_ITEM_REQUEST = 7


	val category = arrayOf("Foodstuff" , "Supermarket item" , "Alcoholic Drink", "Pharmaceutical Product")

		fun  getFileExtension(activity: Activity, uri: Uri?): String?{
		return  MimeTypeMap.getSingleton()
			.getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
	}
}