package com.sais.deliveryapp.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.sais.deliveryapp.databinding.ActivityUploadItemBinding
import com.sais.deliveryapp.frebase.FireStore
import com.sais.deliveryapp.models.Business
import com.sais.deliveryapp.models.Item
import com.sais.deliveryapp.models.Items
import com.sais.deliveryapp.utils.Constants

class UploadItem : BaseActivity() {

	lateinit var binding:ActivityUploadItemBinding
	private lateinit var db: FirebaseFirestore
	private var title: String? = null
	private var bsName: String? = null
	private var category: String? = null
	private var quantity: String? = null
	private var description: String? = null
	private var price: Int? = null
	lateinit var business: Business

	override fun onCreate(savedInstanceState: Bundle?) {
		binding = ActivityUploadItemBinding.inflate(layoutInflater)
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
		db = FirebaseFirestore.getInstance()

		if (intent.hasExtra(Constants.EXTRA_BUSINESS_INFO)) {
			business = intent.getParcelableExtra<Business>(Constants.EXTRA_BUSINESS_INFO)!! as Business
		}

		binding.btSubmit.setOnClickListener { uploadItems() }
	}

	private fun uploadItems() {
		val id = db.collection(Constants.ITEMS).document().id
		bsName = business.name
		title =  binding.tetTitle.text.toString()
		category =  binding.tetCategory.text.toString()
		quantity =  binding.tetQuantity.text.toString()
		description =  binding.tetBsDesc.text.toString()
		price =  binding.tetBsPrice.text.toString().toInt()

		if (validateInput(title!!, category!!, quantity!!, description!!, price!!)){
			showProgressDialog()
			val item = Item(id, title!!, bsName!!, price!!, quantity!!, description!!, category!!)
			FireStore().saveItem(this, item)
		}
	}

	private fun validateInput(title: String, category: String, quantity: String, desc: String, price:Int): Boolean {
		return when {
			TextUtils.isEmpty(title) -> {
				showErrorSnackBar("Please Enter Item's Title")
				false
			}
			TextUtils.isEmpty(category) ->{
				showErrorSnackBar("Please Enter Item's Category")
				false
			}
			TextUtils.isEmpty(quantity) -> {
				showErrorSnackBar("Please Enter Item's Quantity")
				false
			}
			TextUtils.isEmpty(desc) -> {
				showErrorSnackBar("Please Enter Item's Description")
				false
			}

			TextUtils.isEmpty(price.toString()) -> {
				showErrorSnackBar("Please Enter Item's Price")
				false
			}
			else -> {
				true
			}
		}
	}

	fun saveItemSuccess(){
		hideProgressDialog()
		showToast("Item Uploaded Successfully")
		startActivity(Intent(this, ProfileActivity::class.java))
		finish()
	}
}