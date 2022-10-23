package com.sais.deliveryapp.frebase

import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.sais.deliveryapp.activities.*
import com.sais.deliveryapp.fragments.HomeFragment
import com.sais.deliveryapp.logins.LoginActivity
import com.sais.deliveryapp.logins.RegisterActivity
import com.sais.deliveryapp.models.*
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

	fun saveRetailer(activity : RegisterBusiness, retailerInfo: Retailer){
		db.collection(Constants.RETAILERS)
			.document(getCurrentUserId())
			.set(retailerInfo, SetOptions.merge())
			.addOnSuccessListener {
//				activity.saveRetailerSuccess()
			}
			.addOnFailureListener { error->
				activity.showErrorSnackBar("Failed to register retailer: ${error.message}")
			}
	}

	fun saveBusiness(activity: RegisterBusiness, business: Business){
		db.collection(Constants.BUSINESS)
			.document(business.id)
			.set(business, SetOptions.merge())
			.addOnSuccessListener {
				activity.addBusinessSuccess()
			}
			.addOnFailureListener {
				activity.hideProgressDialog()
				activity.showErrorSnackBar("Error: ${it.message}")
			}
	}

	fun addBusiness(activity: RegisterBusiness, business: Business){
		db.collection(Constants.BUSINESS)
			.document(business.id)
			.set(business, SetOptions.merge())
			.addOnSuccessListener {
				activity.addBusinessSuccess()
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

	fun updateBusiness(activity: RegisterBusiness, businessHashMap: HashMap<String, Any>, business: Business){
		db.collection(Constants.BUSINESS)
			.document(business.id)
			.update(businessHashMap)
			.addOnSuccessListener {
				Toast.makeText(activity, "Updated successfully", Toast.LENGTH_SHORT).show()
				activity.updateBsSuccess()
			}
			.addOnFailureListener { exception ->
				activity.hideProgressDialog()
				Toast.makeText(activity, "${exception.message}", Toast.LENGTH_SHORT).show()
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

	fun updateItem(activity: UploadItem, itemHashMap: HashMap <String, Any>, item: Item){
		db.collection(Constants.ITEMS)
			.document(item.id)
			.update(itemHashMap)
			.addOnSuccessListener {
				Toast.makeText(activity, "Updated successfully", Toast.LENGTH_SHORT).show()
				activity.updateItemSuccess()
			}
			.addOnFailureListener { exception ->
				activity.hideProgressDialog()
				Toast.makeText(activity, "${exception.message}", Toast.LENGTH_SHORT).show()
			}
	}

	fun fetchCartItems(activity: CartActivity){
		db.collection(Constants.CART_ITEM)
			.get()
			.addOnSuccessListener {
				val itemsList : ArrayList<CartItem> = ArrayList()
				for (i in it){
					val item = i.toObject(CartItem::class.java)
					item.id = i.id
					itemsList.add(item)
				}
				activity.fetchCartItems(itemsList)
			}
			.addOnFailureListener {
				activity.showToast(it.message!!)
			}
	}

	fun deleteItem(activity: ItemsActivity, item: Item){
		db.collection(Constants.ITEMS)
			.document(item.id)
			.delete()
			.addOnSuccessListener {
				activity.deleteSuccess()
			}
			.addOnFailureListener {
				activity.hideProgressDialog()
				activity.showErrorSnackBar("${it.message}")
			}
	}
}