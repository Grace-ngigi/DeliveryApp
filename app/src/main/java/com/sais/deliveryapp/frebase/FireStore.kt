package com.sais.deliveryapp.frebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.sais.deliveryapp.activities.*
import com.sais.deliveryapp.logins.LoginActivity
import com.sais.deliveryapp.logins.RegisterActivity
import com.sais.deliveryapp.models.Business
import com.sais.deliveryapp.models.Item
import com.sais.deliveryapp.models.Items
import com.sais.deliveryapp.models.Retailer
import com.sais.deliveryapp.utils.Constants

class FireStore {
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

	fun sendEmailVerification(activity: RegisterActivity) {
		val retailer = mAuth.currentUser
		retailer!!.sendEmailVerification()
			.addOnCompleteListener { task ->
				if (task.isSuccessful) {
					activity.showToast("Verification email sent to " + retailer.email)
					activity.sendEmailVerificationSuccess()
				} else {
					activity.showErrorSnackBar("Failed to send verification email: ${task.exception}")
				}
			}
	}

	fun checkEmailVerified(activity: LoginActivity){
		val user = mAuth.currentUser
		if (user != null) {
			if (user.isEmailVerified) {
				activity.emailVerifiedSuccess()
			} else {
				activity.emailVerifiedFailure()
			}
		}
	}

	fun saveRetailerInfo(activity : RegisterActivity, retailerInfo: Retailer){
		db.collection(Constants.RETAILERS)
			.document(getCurrentUserId())
			.set(retailerInfo, SetOptions.merge())
			.addOnSuccessListener {
				activity.saveRetailerSuccess()
			}
			.addOnFailureListener { error->
				activity.saveRetailerFailure()
				activity.showErrorSnackBar("Failed to register retailer: ${error.message}")
			}
	}

	fun saveBsProfile(activity: RegisterBusiness, businessInfo: Business){
		db.collection(Constants.BUSINESS)
			.document(businessInfo.id)
			.set(businessInfo, SetOptions.merge())
			.addOnSuccessListener {
				activity.saveBusinessSuccess()
			}
			.addOnFailureListener {
				activity.hideProgressDialog()
				activity.showErrorSnackBar("Error: ${it.message}")
			}
	}

	fun fetchBusiness(activity: ProfileActivity){
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
				activity.showErrorSnackBar("${it.message}")
			}
	}

	fun saveItem(activity: UploadItem, item: Item){
		db.collection(Constants.ITEMS)
			.document(item.id)
			.set(item, SetOptions.merge())
			.addOnSuccessListener {
				activity.saveItemSuccess()
			}
			.addOnFailureListener {
				activity.hideProgressDialog()
				activity.showErrorSnackBar("${it.message}")
			}
	}

	fun fetchItems(activity:ItemsActivity, business: Business){
		db.collection(Constants.ITEMS)
			.whereEqualTo("bsId", business.id)
			.get()
			.addOnSuccessListener {
				val itemList : ArrayList<Item> = ArrayList()
				for (i in it.documents){
					val item = i.toObject(Item::class.java)
					item!!.id = i.id
					itemList.add(item)
				}
				activity.itemsDetails(itemList)
			}
			.addOnFailureListener {
				activity.hideProgressDialog()
				activity.showErrorSnackBar("${it.message}")
			}

	}

	fun fetchAllItems(activity:MainActivity){
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
				activity.hideProgressDialog()
				activity.showErrorSnackBar("${it.message}")
			}
	}
}