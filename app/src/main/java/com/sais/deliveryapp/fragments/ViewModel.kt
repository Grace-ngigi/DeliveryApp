package com.sais.deliveryapp.fragments

import android.app.Dialog
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.sais.deliveryapp.R
import com.sais.deliveryapp.activities.*
import com.sais.deliveryapp.models.Business
import com.sais.deliveryapp.models.CartItem
import com.sais.deliveryapp.models.Item
import com.sais.deliveryapp.models.Retailer
import com.sais.deliveryapp.utils.Constants
import kotlin.contracts.contract

class ViewModel: ViewModel() {
	private val db = FirebaseFirestore.getInstance()
	private val mAuth = FirebaseAuth.getInstance()

	fun getCurrentUserId(): String {
		val currentUser = mAuth.currentUser
		var currentUserId = ""
		if (currentUser != null) {
			currentUserId = currentUser.uid
		}
		return currentUserId
	}

	fun fetchBusiness(fragment: AccountFragment){
		db.collection(Constants.BUSINESS)
			.whereEqualTo("retailerId", getCurrentUserId())
			.get()
			.addOnSuccessListener {
					document->
				val businessList: ArrayList<Business> = ArrayList()
				for (i in document.documents){

					val bs = i.toObject(Business::class.java)
					bs!!.id = i.id
					businessList.add(bs)
				}
				fragment.businessDetails(businessList)
			}
			.addOnFailureListener {
//				fragment.showErrorSnackBar("${it.message}")
			}
	}

	fun fetchBusinessProfile(fragment: AccountFragment){
		db.collection(Constants.RETAILERS)
			.whereEqualTo("id", getCurrentUserId())
			.get()
			.addOnSuccessListener {
					document->
				val businessList: ArrayList<Retailer> = ArrayList()
				for (i in document.documents){
					val bs = i.toObject(Retailer::class.java)
					bs!!.id = i.id
					businessList.add(bs)
				}
				fragment.bsProfileDetails(businessList)
			}
			.addOnFailureListener {
//				fragment.showErrorSnackBar("${it.message}")
			}
	}

	fun fetchAllItems(fragment: HomeFragment){
		db.collection(Constants.ITEMS)
			.get()
			.addOnSuccessListener {
				val itemsList : ArrayList<Item> = ArrayList()
				for (i in it){
					val item = i.toObject(Item::class.java)
					item.id = i.id
					itemsList.add(item)
				}
				fragment.allItems(itemsList)
			}
			.addOnFailureListener {
//				fragment.hideProgressDialog()
//				fragment.showErrorSnackBar("${it.message}")
			}
	}

	fun addToCart(fragment: HomeFragment, cartItem:CartItem){
		db.collection(Constants.CART_ITEM)
			.document(cartItem.id)
			.set(cartItem, SetOptions.merge())
			.addOnSuccessListener {
				fragment.addToCartSuccess()
			}
			.addOnFailureListener {
				it.message?.let { it1 -> fragment.addToCartFailure(it1) }
			}
	}
}