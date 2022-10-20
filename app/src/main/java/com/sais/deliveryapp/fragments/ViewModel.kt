package com.sais.deliveryapp.fragments

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.sais.deliveryapp.activities.*
import com.sais.deliveryapp.models.Business
import com.sais.deliveryapp.models.Item
import com.sais.deliveryapp.models.Retailer
import com.sais.deliveryapp.utils.Constants

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

	fun fetchBusiness(activity: AccountFragment){
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
				activity.businessDetails(businessList)
			}
			.addOnFailureListener {
//				activity.showErrorSnackBar("${it.message}")
			}
	}

	fun fetchBusinessProfile(activity: AccountFragment){
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
				activity.bsProfileDetails(businessList)
			}
			.addOnFailureListener {
//				activity.showErrorSnackBar("${it.message}")
			}
	}

	fun fetchAllItems(activity: HomeFragment){
		db.collection(Constants.ITEMS)
			.get()
			.addOnSuccessListener {
				val itemsList : ArrayList<Item> = ArrayList()
				for (i in it){
					val item = i.toObject(Item::class.java)
					item.id = i.id
					itemsList.add(item)
				}
				activity.allItems(itemsList)
			}
			.addOnFailureListener {
//				activity.hideProgressDialog()
//				activity.showErrorSnackBar("${it.message}")
			}
	}
}